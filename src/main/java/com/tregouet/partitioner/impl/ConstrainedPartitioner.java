package com.tregouet.partitioner.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tregouet.partitioner.IPartitioner;

public class ConstrainedPartitioner<T> extends Partitioner<T> implements IPartitioner<T> {

	private List<List<T>> authorizedSubsets = new ArrayList<>();
	
	public ConstrainedPartitioner(Set<T> set, Set<Set<T>> authorizedSubsets) {
		super(set);
		for (Set<T> subset : authorizedSubsets) {
			List<T> sortedSubset = new ArrayList<>(subset);
			Collections.sort(sortedSubset, Comparator.comparing(t -> setAsList.indexOf(t)));
		}
	}
	
	protected ConstrainedPartitioner(Set<T> set, List<List<T>> authorizedSubsets) {
		super(set);
		this.authorizedSubsets = authorizedSubsets;
	}	
	
	protected ConstrainedPartitioner(List<T> set, List<List<T>> authorizedSubsets) {
		super(set);
		this.authorizedSubsets = authorizedSubsets;
	}	
	
	@Override
	public List<List<List<T>>> getAllPartitions(){
		List<List<List<T>>> partitions = new ArrayList<>();
		boolean partitionIsValid;
		List<List<T>> partition;
		boolean noMoreSubsets;
		int subsetIdx;
		do {
			partition = new ArrayList<>();
			noMoreSubsets = false;
			subsetIdx = 0;
			do {
				partitionIsValid = true;
				List<T> nextSubset = new ArrayList<>();
				for (int i = 0 ; i < partitionEncoding.length ; i++) {
					if (partitionEncoding[i] == subsetIdx)
						nextSubset.add(setAsList.get(i));
				}
				if (!authorizedSubsets.containsAll(nextSubset))
					partitionIsValid = false;
				else {
					if (nextSubset.isEmpty()) {
						partitions.add(partition);
						noMoreSubsets = true;
					}
					else {
						partition.add(nextSubset);
						subsetIdx++;
					}
				}
			} while (!noMoreSubsets && partitionIsValid);			
		} while (advance());
		return partitions;
	}
	
	@Override
	public List<List<Set<T>>> getAllPartitionsAsListsOfSets(){
		List<List<Set<T>>> partitions = new ArrayList<>();
		boolean partitionIsValid;
		List<Set<T>> partition;
		boolean noMoreSubsets;
		int subsetIdx;
		do {
			partition = new ArrayList<>();
			noMoreSubsets = false;
			subsetIdx = 0;
			do {
				partitionIsValid = true;
				List<T> nextSubset = new ArrayList<>();
				for (int i = 0 ; i < partitionEncoding.length ; i++) {
					if (partitionEncoding[i] == subsetIdx)
						nextSubset.add(setAsList.get(i));
				}
				if (!authorizedSubsets.containsAll(nextSubset))
					partitionIsValid = false;
				else {
					if (nextSubset.isEmpty()) {
						partitions.add(partition);
						noMoreSubsets = true;
					}
					else {
						partition.add(new HashSet<>(nextSubset));
						subsetIdx++;
					}
				}
			} while (!noMoreSubsets && partitionIsValid);			
		} while (advance());
		return partitions;
	}
	
	@Override
	protected IPartitioner<T> getNewPartitioner(Set<T> set) {
		List<List<T>> authorizedSubsets = new ArrayList<>();
		for (List<T> authorized : this.authorizedSubsets) {
			if (set.containsAll(authorized))
				authorizedSubsets.add(authorized);
		}
		return new ConstrainedPartitioner<T>(setAsList, authorizedSubsets);
	}
	
	@Override
	protected IPartitioner<T> getNewPartitioner(List<T> setAsList) {
		List<List<T>> authorizedSubsets = new ArrayList<>();
		for (List<T> authorized : this.authorizedSubsets) {
			if (setAsList.containsAll(authorized))
				authorizedSubsets.add(authorized);
		}
		return new ConstrainedPartitioner<T>(setAsList, authorizedSubsets);
	}	

}
