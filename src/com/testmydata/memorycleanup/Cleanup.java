package com.testmydata.memorycleanup;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.javafx.event.EventQueue;
import com.testmydata.util.SchedulerTime;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class Cleanup {

	@SuppressWarnings("rawtypes")
	public static void nullifyStrings(Object o) {

		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(String.class)) {
					String value = (String) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(ArrayList.class)) {
					ArrayList value = (ArrayList) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(EventQueue.class)) {
					EventQueue value = (EventQueue) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(BufferedImage.class)) {
					BufferedImage value = (BufferedImage) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(File.class)) {
					File value = (File) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(FileInputStream.class)) {
					FileInputStream value = (FileInputStream) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(URL.class)) {
					URL value = (URL) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(DateFormat.class)) {
					DateFormat value = (DateFormat) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(ParseException.class)) {
					ParseException value = (ParseException) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(SimpleDateFormat.class)) {
					SimpleDateFormat value = (SimpleDateFormat) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(Instant.class)) {
					Instant value = (Instant) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(LocalDate.class)) {
					LocalDate value = (LocalDate) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(LocalDateTime.class)) {
					LocalDateTime value = (LocalDateTime) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(ZoneId.class)) {
					ZoneId value = (ZoneId) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(ZonedDateTime.class)) {
					ZonedDateTime value = (ZonedDateTime) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(DateTimeFormatter.class)) {
					DateTimeFormatter value = (DateTimeFormatter) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(Date.class)) {
					Date value = (Date) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(HashMap.class)) {
					HashMap value = (HashMap) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(Properties.class)) {
					Properties value = (Properties) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(ResourceBundle.class)) {
					ResourceBundle value = (ResourceBundle) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(Vector.class)) {
					Vector value = (Vector) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(Matcher.class)) {
					Matcher value = (Matcher) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(Pattern.class)) {
					Pattern value = (Pattern) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(ChangeListener.class)) {
					ChangeListener value = (ChangeListener) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(ObservableValue.class)) {
					ObservableValue value = (ObservableValue) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(FXCollections.class)) {
					FXCollections value = (FXCollections) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(ObservableList.class)) {
					ObservableList value = (ObservableList) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(SwingFXUtils.class)) {
					SwingFXUtils value = (SwingFXUtils) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(ActionEvent.class)) {
					ActionEvent value = (ActionEvent) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(EventHandler.class)) {
					EventHandler value = (EventHandler) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(EventHandler.class)) {
					EventHandler value = (EventHandler) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(Button.class)) {
					Button value = (Button) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(ComboBox.class)) {
					ComboBox value = (ComboBox) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(Hyperlink.class)) {
					Hyperlink value = (Hyperlink) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(Label.class)) {
					Label value = (Label) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(TableCell.class)) {
					TableCell value = (TableCell) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(TableColumn.class)) {
					TableColumn value = (TableColumn) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(CellEditEvent.class)) {
					CellEditEvent value = (CellEditEvent) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(TableView.class)) {
					TableView value = (TableView) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(TextArea.class)) {
					TextArea value = (TextArea) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(TextField.class)) {
					TextField value = (TextField) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(PropertyValueFactory.class)) {
					PropertyValueFactory value = (PropertyValueFactory) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(TextFieldTableCell.class)) {
					TextFieldTableCell value = (TextFieldTableCell) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(DropShadow.class)) {
					DropShadow value = (DropShadow) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(Reflection.class)) {
					Reflection value = (Reflection) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(Image.class)) {
					Image value = (Image) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(ImageView.class)) {
					ImageView value = (ImageView) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().equals(AnchorPane.class)) {
					AnchorPane value = (AnchorPane) f.get(o);
					if (value != null) {
						f.set(o, null);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		SchedulerTime it = new SchedulerTime();
		it.run();
	}
}
