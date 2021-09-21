package com.tregouet.partitioner.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.tregouet.partitioner.IPartitioner;

public class Partitioner<T> implements IPartitioner<T> {

	List<T> setAsList;
	int[] partitionEncoding;
	int rightMostIncrementableIdx;
	
	public Partitioner(Set<T> set) {
		setAsList = new ArrayList<>(set);
		partitionEncoding = new int[setAsList.size()];
	}
	
	/**
	 * Unsafe : no unicity check
	 * @param setAsList
	 */
	private Partitioner(List<T> setAsList) {
		this.setAsList = setAsList;
		partitionEncoding = new int[setAsList.size()];
	}

	/**
	 * Based on Rici's answer to this question in stackoverflow.com : 
	 * https://stackoverflow.com/questions/30893292/generate-all-partitions-of-a-set 
	 */
	@Override
	public List<List<List<T>>> getAllPartitions() {
		List<List<List<T>>> partitions = new ArrayList<>();
		do {
			List<List<T>> partition = new ArrayList<>();
			boolean noMoreSubsets = false;
			int subsetIdx = 0;
			do {
				List<T> nextSubset = new ArrayList<>();
				for (int i = 0 ; i < partitionEncoding.length ; i++) {
					if (partitionEncoding[i] == subsetIdx)
						nextSubset.add(setAsList.get(i));
				}
				if (nextSubset.isEmpty()) {
					partitions.add(partition);
					noMoreSubsets = true;
				}
				else {
					partition.add(nextSubset);
					subsetIdx++;
				}
			} while (!noMoreSubsets);			
		} while (advance());
		return partitions;
	}

	/**
	 * https://en.wikipedia.org/wiki/Hierarchy_(mathematics)
	 * Spanning hierarchy : for n elements, contains (at least) the set of n elements and n sets 
	 * of 1 elements.
	 */
	@Override
	public List<List<List<T>>> getAllSpanningHierarchies() {
		List<List<List<T>>> hierarchies = new ArrayList<>();
		if (setAsList.size() == 1) {
			//add the size 1 list of hierarchies of a size 1 set
			List<List<T>> hierarchyOfSize1Set = new ArrayList<>();
			hierarchyOfSize1Set.add(setAsList);
			hierarchies.add(hierarchyOfSize1Set);
		}
		else {
			for (List<List<T>> partition : getAllPartitions()) {
				if (partition.size() > 1) {
					/*
					 * LLL[L] -	a sub-subset
					 * LL[L]L -	a hierarchy of a subset
					 * L[L]LL -	all hierarchies of a subset
					 * [L]LLL -	for each subset of a partition, all hierarchies
					 */
					List<List<List<List<T>>>> hierarchiesOfEachSubset = new ArrayList<>();
					for (List<T> subset : partition) {
						List<List<List<T>>> hierarchiesOfCurrSubset = 
								new Partitioner<T>(subset).getAllSpanningHierarchies();
						hierarchiesOfEachSubset.add(hierarchiesOfCurrSubset);
					}
					/*
					 * For any partition P of a set S (with P ≠ S), a spanning hierarchy is S plus any 
					 * element of the cartesian product of the lists of spanning hierarchies of P's subsets. 
					 */
					for(List<List<List<T>>> forEachSubsetOneHierarchy : 
							Lists.cartesianProduct(hierarchiesOfEachSubset)) {
						List<List<T>> nextHierarchy = new ArrayList<>();
						nextHierarchy.add(this.setAsList);
						for (List<List<T>> hierarchyOfOneSubset : forEachSubsetOneHierarchy) {
							for (List<T> subSubset : hierarchyOfOneSubset)
								nextHierarchy.add(subSubset);
						}
						hierarchies.add(nextHierarchy);	
					}
				}
			}
		}
		return hierarchies;
	}	
	
	private boolean advance() {
		rightMostIncrementableIdx = getIndexOfTheRightmostIncrementableElement();
		if (rightMostIncrementableIdx == -1)
			return false;
		else {
			partitionEncoding[rightMostIncrementableIdx]++;
			setSuffixAtMinimalValue(rightMostIncrementableIdx);
			return true;
		}
	}
	
	private int getIndexOfTheRightmostIncrementableElement() {
		//the first element is never incrementable
		for (int i = partitionEncoding.length - 1 ; i > 0 ; i--) {
			int iValue = partitionEncoding[i];
			if (partitionPrefixContains(iValue, i)) {
				return i;
			}
		}
		return -1;
	}
	
	private boolean partitionPrefixContains(int value, int exclusiveUpperBound) {
		for (int i = 0 ; i < exclusiveUpperBound ; i++) {
			if (partitionEncoding[i] == value)
				return true;
		}
		return false;
	}
	
	private void setSuffixAtMinimalValue(int exclusiveLowerBound) {
		for (int i = exclusiveLowerBound + 1 ; i < partitionEncoding.length ; i++) {
			partitionEncoding[i] = 0;
		}
	}

}
