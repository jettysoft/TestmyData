package com.testmydata.util;

import java.util.Comparator;

import com.testmydata.binarybeans.FieldtoFieldBinaryTrade;;

public class CustomComparator implements Comparator<FieldtoFieldBinaryTrade> {
	@Override
	public int compare(FieldtoFieldBinaryTrade o1, FieldtoFieldBinaryTrade o2) {
		return o2.getCreateddate().compareTo(o1.getCreateddate());
	}
}