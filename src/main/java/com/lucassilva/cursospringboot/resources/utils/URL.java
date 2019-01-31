package com.lucassilva.cursospringboot.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class URL {
	
	public static String decodeParam(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	

	public static List<Integer> decodeIntList(String string) {

		String[] s = string.split(",");

		return Arrays.asList(s).stream().map(Integer::parseInt).collect(Collectors.toList());

	}

}
