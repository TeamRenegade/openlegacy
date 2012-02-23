package org.openlegacy.terminal;

import org.openlegacy.terminal.spi.TerminalSendAction;

/**
 * Define a terminal session override-able methods which happens before & after a terminal session action
 * 
 */
public interface TerminalConnectionListener {

	void beforeConnect(TerminalConnection terminalConnection);

	void afterConnect(TerminalConnection terminalConnection);

	void beforeSendAction(TerminalConnection terminalConnection, TerminalSendAction terminalSendAction);

	void afterSendAction(TerminalConnection terminalConnection);

}