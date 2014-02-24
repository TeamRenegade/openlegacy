package org.h3270.host;

/*
 * Copyright (C) 2003-2008 akquinet tech@spree
 *
 * This file is part of h3270.
 *
 * h3270 is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * h3270 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with h3270; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, 
 * MA 02110-1301 USA
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.h3270.render.TextRenderer;

/**
 * An implementation of the Screen interface that is fed by the
 * output of s3270.
 * 
 * @author Andre Spiegel spiegel@gnu.org
 * @version $Id$
 */
public class S3270Screen extends AbstractScreen {

  private List bufferData = null;
  private String status = null;
  
  public S3270Screen() {
    width  = 0;
    height = 0;
    buffer = null;
    isFormatted = true; 
  }
  
  public S3270Screen (InputStream in) {
    try {
      BufferedReader input =
        new BufferedReader 
          (new InputStreamReader(in,
                                 "ISO-8859-1"));
      List lines = new ArrayList();
      String status = null;
      while (true) {
        String line = input.readLine();
        if (line == null) break;
        if (line.startsWith ("data:"))
          lines.add (line);
        else if (Pattern.matches ("[ULE] [UF] [UC] .*", line))
          status = line;
      }
      update (status, lines);
    } catch (IOException ex) {
      throw new RuntimeException ("error: " + ex);
    } 
  }

  /**
   * Pattern that matches a status line from s3270.
   * Example:   U F U C(hostname) I 3 32 80 22 15 0x0 -
   */
  private static Pattern statusPattern =
    Pattern.compile (  "^[ULE] "             // Keyboard State
                     + "[FU] "               // Formatted / Unformatted
                     + "[PU] "               // Protected / Unprotected (at cursor)
                     + "(?:C\\([^)]*\\)|N) " // Connected / Not Connected
                     + "[ILCN] "             // Emulator Mode
                     + "[2-5] "              // Model Number
                     + "[0-9]+ "             // Number of Rows
                     + "[0-9]+ "             // Number of Columns
                     + "([0-9]+) "           // Cursor Row
                     + "([0-9]+) "           // Cursor Column
                     + "0x0 "                // Window ID (always 0x0)
                     + "(?:[0-9.]+|-)$"      // Time for last command
                    );

  /**
   * Updates this screen with output from "readbuffer ascii".
   * @param status the status line that was returned by s3270
   * @param bufferData the actual screen data, as a list of strings
   */
  public void update (String status, List bufferData) {
    this.status = status;
    if (status.charAt(2) == 'F') {
      isFormatted = true;
      updateBuffer (bufferData);
    } else {
      isFormatted = false;
      updateBuffer (bufferData);
    }
    Matcher m = statusPattern.matcher(status);
    if (m.find()) {
      cursorX = Integer.parseInt (m.group(2));
      cursorY = Integer.parseInt (m.group(1));
      InputField f = getInputFieldAt (cursorX, cursorY);
      if (f != null) 
        f.setFocused (true);
    } else {
      cursorX = 0;
      cursorY = 0;
    }
  }
  
  private void updateBuffer (List bufferData) {
    this.bufferData = new ArrayList (bufferData);
    height = bufferData.size();
    width = 0;
    buffer = new char[height][];
    fields = new ArrayList();
    fieldStartX = 0;
    fieldStartY = 0;
    fieldStartCode = (byte)0xe0;
    
    for (int y=0; y<height; y++) {
      char[] line = decode ((String)bufferData.get(y), y, fields);
      if (line.length > width) width = line.length;
      buffer[y] = line;
    }     
    // add the final field on the page
    fields.add(createField(fieldStartCode, fieldStartX, fieldStartY,
               width - 1, height - 1, color, ext_highlight));
  }
  
  public List getBufferData() {
    return Collections.unmodifiableList (bufferData);
  }
  
  public void dump (String filename) {
    try {
      PrintWriter out = new PrintWriter (new FileWriter (filename));
      for (Iterator i = bufferData.iterator(); i.hasNext();) {
        out.println (i.next());
      }
      out.println (status);
      out.println ("ok");
      out.close();
    } catch (IOException ex) {
      throw new RuntimeException ("error: " + ex);
    } 
  }

  private static final Pattern FORMATTED_CHAR_PATTERN = Pattern.compile (
    "SF\\((..)=(..)(,(..)=(..)(,(..)=(..))?)?\\)|([0-9a-fA-F]{2})+"
  );

  private int  fieldStartX = 0;
  private int  fieldStartY = 0;
  private byte fieldStartCode = (byte)0xe0;

  private int color = Field.ATTR_COL_DEFAULT;
  private int ext_highlight = Field.ATTR_EH_DEFAULT;
  
  /**
   * Decodes a single line from the raw screen buffer dump.
   */
  private char[] decode (String line, int y, List fields) {

    int fieldEndX = 0;
    int fieldEndY = 0;
    int i;
    int aux_startcode = -1;
    int aux_color;
    int aux_exthighlight;
    String aux_code;

    if (line.startsWith("data: ")) line = line.substring(6);
    
    StringBuffer result = new StringBuffer();
    int index = 0;

    // workaround! delete all extended attributes in a line!
    // must have, until h3270 supports extended attributes
    // TODO:    
    line = line.replaceAll("SA\\(..=..\\)", "");

    Matcher m = FORMATTED_CHAR_PATTERN.matcher (line);
    
    while (m.find()) {
      String code = m.group();
      if (code.startsWith ("SF")) {
          
        if (!isFormatted)
          throw new RuntimeException 
            ("format information in unformatted screen");
        result.append (' ');
        i = 1;
        aux_color = -1;
        aux_exthighlight = -1;

        while (i <= m.groupCount()) {
          aux_code = m.group(i);
          if (aux_code == null) break;

          if (aux_code.equals("c0")) {
            if (fieldStartX != -1) {
              // if we've been in an open field, close it now
              fieldEndX = index - 1;
              fieldEndY = y;
              if (fieldEndX == -1) {
                fieldEndX = width-1;
                fieldEndY--;
              }
            }
            aux_startcode = i + 1;                        
          } else if (aux_code.equals("41")) {
            aux_exthighlight = i + 1;
          } else if (aux_code.equals("42")) {
            aux_color = i + 1;
          }
          i = i + 3;
        }

        if (i > 1) {
          if (fieldStartX != -1) {                   
            fields.add (createField (fieldStartCode, fieldStartX,
                                     fieldStartY, fieldEndX, fieldEndY, color,
                                     ext_highlight));
          }            
          fieldStartX = index + 1;
          fieldStartY = y;
          fieldStartCode = (byte) Integer.parseInt(m.group(aux_startcode), 16);
          if (aux_exthighlight != -1) {
            ext_highlight = Integer.parseInt(m.group(aux_exthighlight), 16);
          } else {
            ext_highlight = Field.ATTR_EH_DEFAULT;
          }
          if (aux_color != -1) {
            color = Integer.parseInt(m.group(aux_color), 16);
          } else {
            color = Field.ATTR_COL_DEFAULT;
          }
        }
      } else {
        result.append (decodeChar(code));
      }
      index++;
    }
    // a field that begins in the last column
    if (fieldStartX == index && fieldStartY == y) {
      fieldStartX = 0;
      fieldStartY++;
    }  
    return result.toString().toCharArray(); 
  }
  
  /**
   * CharsetDecoder used by decodeChar() below.  Uses local code page on Windows,
   * and UTF-8 on all other systems.
   */
  private CharsetDecoder charsetDecoder = 
    System.getProperty("os.name").startsWith("Windows")
      ? Charset.defaultCharset().newDecoder()
      : Charset.forName("UTF-8").newDecoder();

  private ByteBuffer codeBuffer = ByteBuffer.allocate(6); // max utf8 length for a single char
  private CharBuffer charBuffer = CharBuffer.allocate(1);
  
  /**
   * Given a hexadecimal representation of a character, return that character.
   * This method assumes the character to be encoded using the local code page
   * on Windows, and UTF-8 on all other systems. 
   */
  private char decodeChar (String source) {
    codeBuffer.clear();
    for (int i=0; i<source.length(); i+=2) {
      int val = value(source.charAt(i)) * 16 + value(source.charAt(i+1));
      codeBuffer.put((byte)val);
    }
    codeBuffer.rewind();
    charBuffer.clear();
    charsetDecoder.reset();
    charsetDecoder.decode(codeBuffer, charBuffer, true);
    charsetDecoder.flush (charBuffer);
    return charBuffer.get(0);
  }
  
  private int value (char c) {
    if       (c >= '0' && c <= '9') return c - 48;
    else if (c >= 'a' && c <= 'f') return c - 87;
    else if (c >= 'A' && c <= 'F') return c - 55;
    else throw new RuntimeException ("cannot happen");
  }
  
  private Field createField(byte startCode, int startx, int starty,
                            int endx, int endy, int color, int ext_highlight) {
	  													
	  if ((startCode & Field.ATTR_PROTECTED) == 0 || (endy- starty >= 23)) // RM 24/02/2014 - workaround to support empty input screen
      return new InputField (this, startCode, startx, starty, endx, endy,
                             color, ext_highlight);
    else
      return new Field (this, startCode, startx, starty, endx, endy,
                        color, ext_highlight);
  }
                              
  public static void main (String[] args) throws IOException {
    BufferedReader in = new BufferedReader 
                          (new FileReader ("test/src/org/h3270/test/hebrew.dump"));
    List lines = new ArrayList();
    while (true) {
      String line = in.readLine();
      if (line == null || !line.startsWith ("data: ")) break;
      lines.add (line.substring(6));
    }
    S3270Screen s = new S3270Screen();
    s.update ("U F U", lines);
    System.out.println (new TextRenderer().render(s));
  }

}
	