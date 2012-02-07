package org.h3270.web;

import org.h3270.host.*;
import org.h3270.render.*;

/**
 * Encapsulates the state for an individual terminal window.
 * 
 * @author Andre Spiegel spiegel@gnu.org
 * @version $Id: TerminalState.java,v 1.3 2007/03/02 09:38:29 spiegel Exp $
 */
public class TerminalState {

  private Terminal terminal = null;
  private boolean useKeypad = false;
  private ColorScheme activeColorScheme = null;
  private String screen = null;
  private Throwable exception = null;

  public TerminalState (boolean useKeypad, ColorScheme activeColorScheme) {
    this.useKeypad = useKeypad;
    this.activeColorScheme = activeColorScheme;
  }
  
  public ColorScheme getActiveColorScheme() {
    return activeColorScheme;
  }
  
  public void setActiveColorScheme(ColorScheme activeColorScheme) {
    this.activeColorScheme = activeColorScheme;
  }
  
  public String getScreen() {
    return screen;
  }
  
  public void setScreen(String screen) {
    this.screen = screen;
  }
  
  public Terminal getTerminal() {
    return terminal;
  }
  public void setTerminal(Terminal terminal) {
    this.terminal = terminal;
  }
  
  public boolean useKeypad() {
    return useKeypad;
  }

  public void useKeypad(boolean useKeypad) {
    this.useKeypad = useKeypad;
  }
  
  public void setException (Throwable exception) {
    this.exception = exception;
  }
  
  public Throwable getException() {
    return exception;
  }
}
