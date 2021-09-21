package com.tregouet.partitioner;

import java.util.List;
import java.util.Set;

public interface IPartitioner<T> {
	
	/**
	 * Lists returned instead of sets for better performance, and because no need of unicity checks;
	 * @param set
	 * @return
	 */
	List<List<List<T>>> getAllPartitions();
	
	List<List<List<T>>> getAllSpanningHierarchies();

}
