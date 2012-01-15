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

import java.util.*;
import java.io.*;
import java.net.URL;

/**
 * Test implementation of the Screen interface.  It reads an ASCII dump
 * of a 3270 screen from a file.  In this file, input fields must be delimited
 * by '{' and '}'.
 * 
 * @deprecated This class has not been updated to h3270 1.2 and beyond.
 *
 * @author Andre Spiegel spiegel@gnu.org
 * @version $Id: FileScreen.java,v 1.12 2008/11/21 14:47:22 spiegel Exp $
 */
public class FileScreen extends AbstractScreen {

  public FileScreen (String filename) {
    try {
        URL url = getClass().getResource(filename);

        Reader in = new InputStreamReader (url.openStream(),
                                         "ISO-8859-1");
      width = 80;
      height = 24;
      buffer = new char[height][width];
      for (int y=0; y<height; y++)
        for (int x=0; x<width; x++)
          buffer[y][x] = ' ';

      for (int y=0; y<height; y++) {
        for (int x=0; x<=width; x++) {
          int ch = in.read();
          if (ch == -1)
            return;
          else if (ch == '\n')
            break;
          else if (ch == '\r') {
                in.read(); // skip \n
                break;
          }
          else if (ch == '{')
            x = x + readField (x, y, in);
          else
          {
            buffer[y][x] = (char)ch;
          }
        }
      }
    } catch (IOException ex) {
      throw new RuntimeException ("IOException while reading file");
    }
  }

  /**
   * Reads a field from the file and inserts it into the internal
   * data structures.  Returns the number of characters read.
   */
  private int readField (int x, int y, Reader in) throws IOException {
    buffer[y][x] = ' '; // replace the delimiter that our caller already read
    int index = x;
    int length = 0;
    StringBuffer value = new StringBuffer();
    while (true) {
      int ch = in.read();
      if (ch == -1)
        throw new RuntimeException ("EOF while reading field");
      else if (ch == '}') {
        buffer[y][index+1] = ' ';
        Field f = new InputField (this, (byte)0,
                                  x+1, y, index, y);
        fields.add (f);
        return length + 1;
      } else {
        index++;
        length++;
        value.append ((char)ch);
        buffer[y][index] = (char)ch;
      }
    }
  }

  public static void main (String[] args) {
    FileScreen f = new FileScreen ("src/org/h3270/test/screen1.txt");
    for (int y = 0; y < f.getHeight(); y++) {
      for (int x = 0; x < f.getWidth(); x++) {
        System.out.print (f.charAt(x, y));
      }
      System.out.println();
    }
    System.out.println();
    System.out.println("Fields: ");
    for (Iterator i = f.getFields().iterator(); i.hasNext();) {
      System.out.println (i.next());
    }
  }

}
