package com.tregouet.partitioner.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.tregouet.partitioner.IPartitioner;

public class Partitioner<T> implements IPartitioner<T> {

	protected final Set<T> set;
	protected final List<T> order;
	protected final int[] partitionEncoding;
	private int rightMostIncrementableIdx;
	
	public Partitioner(Set<T> set) {
		this.set = set;
		order = new ArrayList<>(set);
		partitionEncoding = new int[order.size()];
	}
	
	/**
	 * Unsafe : no unicity check
	 * @param setAsList
	 */
	protected Partitioner(List<T> setAsList) {
		set = new HashSet<>(setAsList);
		this.order = setAsList;
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
						nextSubset.add(order.get(i));
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
	
	@Override
	public List<List<Set<T>>> getAllPartitionsAsListsOfSets() {
		List<List<Set<T>>> partitions = new ArrayList<>();
		do {
			List<Set<T>> partition = new ArrayList<>();
			boolean noMoreSubsets = false;
			int subsetIdx = 0;
			do {
				Set<T> nextSubset = new HashSet<>();
				for (int i = 0 ; i < partitionEncoding.length ; i++) {
					if (partitionEncoding[i] == subsetIdx)
						nextSubset.add(order.get(i));
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
	public List<List<List<T>>> getAllSpanningHierarchiesAsListsOfLists() {
		List<List<List<T>>> hierarchies = new ArrayList<>();
		if (order.size() == 1) {
			//add the size 1 list of hierarchies of a size 1 set
			List<List<T>> hierarchyOfSize1Set = new ArrayList<>();
			hierarchyOfSize1Set.add(order);
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
								getNewPartitioner(subset).getAllSpanningHierarchiesAsListsOfLists();
						hierarchiesOfEachSubset.add(hierarchiesOfCurrSubset);
					}
					/*
					 * For any partition P of a set S (with P ≠ S), a spanning hierarchy is S plus any 
					 * element of the cartesian product of the lists of spanning hierarchies of P's subsets. 
					 */
					for(List<List<List<T>>> forEachSubsetOneHierarchy : 
							Lists.cartesianProduct(hierarchiesOfEachSubset)) {
						List<List<T>> nextHierarchy = new ArrayList<>();
						nextHierarchy.add(this.order);
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
	
	/**
	 * https://en.wikipedia.org/wiki/Hierarchy_(mathematics)
	 * Spanning hierarchy : for n elements, contains (at least) the set of n elements and n sets 
	 * of 1 elements.
	 */
	@Override
	public List<List<Set<T>>> getAllSpanningHierarchies() {
		List<List<Set<T>>> hierarchies = new ArrayList<>();
		if (order.size() == 1) {
			//add the size 1 list of hierarchies of a size 1 set
			List<Set<T>> hierarchyOfSize1Set = new ArrayList<>();
			hierarchyOfSize1Set.add(set);
			hierarchies.add(hierarchyOfSize1Set);
		}
		else {
			for (List<Set<T>> partition : getAllPartitionsAsListsOfSets()) {
				if (partition.size() > 1) {
					/*
					 * LLL[S] -	a sub-subset
					 * LL[L]S -	a hierarchy of a subset
					 * L[L]LS -	all hierarchies of a subset
					 * [L]LLS -	for each subset of a partition, all hierarchies
					 */
					List<List<List<Set<T>>>> hierarchiesOfEachSubset = new ArrayList<>();
					for (Set<T> subset : partition) {
						List<List<Set<T>>> hierarchiesOfCurrSubset = 
								getNewPartitioner(subset).getAllSpanningHierarchies();
						hierarchiesOfEachSubset.add(hierarchiesOfCurrSubset);
					}
					/*
					 * For any partition P of a set S (with P ≠ S), a spanning hierarchy is S plus any 
					 * element of the cartesian product of the lists of spanning hierarchies of P's subsets. 
					 */
					for(List<List<Set<T>>> forEachSubsetOneHierarchy : 
							Lists.cartesianProduct(hierarchiesOfEachSubset)) {
						List<Set<T>> nextHierarchy = new ArrayList<>();
						nextHierarchy.add(this.set);
						for (List<Set<T>> hierarchyOfOneSubset : forEachSubsetOneHierarchy) {
							for (Set<T> subSubset : hierarchyOfOneSubset)
								nextHierarchy.add(subSubset);
						}
						hierarchies.add(nextHierarchy);	
					}
				}
			}
		}
		return hierarchies;
	}		
	
	protected boolean advance() {
		rightMostIncrementableIdx = getIndexOfTheRightmostIncrementableElement();
		if (rightMostIncrementableIdx == -1)
			return false;
		else {
			partitionEncoding[rightMostIncrementableIdx]++;
			setSuffixAtMinimalValue(rightMostIncrementableIdx);
			return true;
		}
	}
	
	protected int getIndexOfTheRightmostIncrementableElement() {
		//the first element is never incrementable
		for (int i = partitionEncoding.length - 1 ; i > 0 ; i--) {
			int iValue = partitionEncoding[i];
			if (partitionPrefixContains(iValue, i)) {
				return i;
			}
		}
		return -1;
	}
	
	protected boolean partitionPrefixContains(int value, int strictUpperBound) {
		for (int i = 0 ; i < strictUpperBound ; i++) {
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
	
	protected IPartitioner<T> getNewPartitioner(Set<T> set) {
		return new Partitioner<T>(set);
	}
	
	protected IPartitioner<T> getNewPartitioner(List<T> setAsList) {
		return new Partitioner<T>(setAsList);
	}	

}
