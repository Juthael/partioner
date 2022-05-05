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
	
	public List<List<Set<T>>> getAllPartitionsAsListsOfSets();
	
	List<List<List<T>>> getAllSpanningHierarchiesAsListsOfLists();
	
	public List<List<Set<T>>> getAllSpanningHierarchies();

}
