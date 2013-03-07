package io.ous.concurrentunit.testutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CartesianParameters {
	private List<List<?>> lists;
	public CartesianParameters() {
		lists = new ArrayList<List<?>>();
	}
	public<T> CartesianParameters add(T[] parameters) {
		lists.add(Arrays.asList(parameters));
		return this;
	}
	public Collection<Object[]> toParameters() {
		List<Object[]> product = new ArrayList<Object[]>();
		if(lists.isEmpty()) {
			return product;
		}
		if(lists.size() == 1) {
			for(Object param : lists.get(0)) {
				product.add(new Object[] {param});
			}
			return product;
		}
		
		List<List<?>> reverseLists = new ArrayList<List<?>>(lists);
		Collections.reverse(reverseLists);
		
		
		List<List<Object>> cartesian = cartesianProduct(0, reverseLists);
		for(List<Object> row : cartesian) {
			product.add(row.toArray());
		}
		
		return product;
	}
	

	private static List<List<Object>> cartesianProduct(int index, List<List<?>> lists) { //http://stackoverflow.com/a/714256/777203
	    List<List<Object>> ret = new ArrayList<List<Object>>();
	    if (index == lists.size()) {
	        ret.add(new ArrayList<Object>());
	    } else {
	        for (Object obj : lists.get(index)) {
	            for (List<Object> list : cartesianProduct(index+1, lists)) {
	                list.add(obj);
	                ret.add(list);
	            }
	        }
	    }
	    return ret;
	}
}
