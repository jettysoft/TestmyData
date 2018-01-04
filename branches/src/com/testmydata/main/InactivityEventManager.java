package com.testmydata.main;

import java.beans.PropertyChangeListener;

import javax.swing.Action;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class InactivityEventManager implements Action {
	@FXML
	private static UserDashboard userHome = null;
	@FXML
	static ActionEvent event;

	public void actionPerformed(ActionEvent arg0) {

		LogOut logOut = new LogOut();
		logOut.lockSession(event);

	}

	public static UserDashboard getInstance(ActionEvent event1) {
		event = event1;

		return userHome;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener arg0) {

	}

	@Override
	public Object getValue(String arg0) {

		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void putValue(String arg0, Object arg1) {

	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener arg0) {

	}

	@Override
	public void setEnabled(boolean arg0) {

	}

	public static void main(String[] args) {

	}

	@Override
	public void actionPerformed(java.awt.event.ActionEvent arg0) {

	}

}
