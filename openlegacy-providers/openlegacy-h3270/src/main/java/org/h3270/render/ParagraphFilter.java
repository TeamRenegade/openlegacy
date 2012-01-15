package org.h3270.render;

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

import java.util.regex.*;

public class ParagraphFilter implements Filter {

  private static Pattern paragraphBreak = 
    Pattern.compile ("\\n *\\n", Pattern.MULTILINE);

  private static Pattern hyphenation =
    Pattern.compile ("([a-z])- *\\n *");

  public String filter(String input) {
    Matcher m = paragraphBreak.matcher(input);
    String result = m.replaceAll("\n<p>\n");
    m = hyphenation.matcher(result);
    return m.replaceAll("$1");
  }

}
