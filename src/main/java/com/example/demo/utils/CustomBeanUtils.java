package com.example.demo.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class CustomBeanUtils {

	private CustomBeanUtils() {

	}

	public static void copyProperties(Object source, Object target) {
		BeanUtils.copyProperties(source, target);
	}

	public static void copyNonNullProperties(Object source, Object target) {
		BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
	}

	public static String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<>();
		for (java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());

			if (srcValue == null) {
				emptyNames.add(pd.getName());
			}
		}
		return emptyNames.toArray(new String[emptyNames.size()]);
	}

	public static boolean checkIfAllFieldsAreNull(Object object) {
		final BeanWrapper src = new BeanWrapperImpl(object);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		for (java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());

			if ("class".equals(pd.getName()))
				continue;

			if (srcValue instanceof Collection<?>) {
				Collection<?> list = (Collection<?>) srcValue;
				if (!list.isEmpty()) {
					return false;
				}
			} else if (srcValue != null) {
				return false;
			}
		}

		return true;
	}

}
