package com.testmydata.util;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.testmydata.binarybeans.DBConfigBinaryTade;

public class DBConfigJAXB {
	public static void main(String[] args) {

	}

	public boolean generateDBConfig(DBConfigBinaryTade dbConfigBinaryTade) {
		try {

			File file = new File("conf" + File.separator + "dbconfig.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(DBConfigBinaryTade.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(dbConfigBinaryTade, file);
			// jaxbMarshaller.marshal(dbConfig, System.out);

			return true;
		} catch (JAXBException e) {
			e.printStackTrace();
			return false;
		}

	}

	public DBConfigBinaryTade readDBConfig() throws FileNotFoundException {
		try {

			File file = new File("conf" + File.separator + "dbconfig.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(DBConfigBinaryTade.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			DBConfigBinaryTade que = (DBConfigBinaryTade) jaxbUnmarshaller.unmarshal(file);

			return que;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
}