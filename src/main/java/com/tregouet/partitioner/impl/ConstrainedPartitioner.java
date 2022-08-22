package com.tregouet.partitioner.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.tregouet.partitioner.IPartitioner;

public class ConstrainedPartitioner<T> extends Partitioner<T> implements IPartitioner<T> {

	private Set<List<T>> authorizedSubsets;
	private Integer maxNbOfSubsets;

	public ConstrainedPartitioner(Set<T> set, Set<Set<T>> authorizedSubsets, Integer maxNbOfSubsets) {
		super(set);
		Comparator<T> sorter = ((t1, t2) -> order.indexOf(t1) - order.indexOf(t2));
		if (authorizedSubsets != null) {
			this.authorizedSubsets = new HashSet<>();
			for (Set<T> authorizedSubset : authorizedSubsets) {
				List<T> authorizedSubsetList = new ArrayList<>(authorizedSubset);
				Collections.sort(authorizedSubsetList, sorter);
				this.authorizedSubsets.add(authorizedSubsetList);
			}	
		}
		this.maxNbOfSubsets = maxNbOfSubsets;
	}
	
	/**
	 * UNSAFE. The order over the set of elements must also be the order over every authorized subset.
	 */
	public ConstrainedPartitioner(List<T> set, @Nullable Set<List<T>> authorizedSubsets, @Nullable Integer maxNbOfSubsets) {
		super(set);
		this.authorizedSubsets = authorizedSubsets;
		this.maxNbOfSubsets = maxNbOfSubsets;
	}	

	/**
	 * UNSAFE. The order over the set of elements must also be the order over every authorized subset.
	 */
	private ConstrainedPartitioner(List<T> set, List<List<T>> authorizedSubsets, Integer maxNbOfSubsets) {
		super(set);
		if (authorizedSubsets != null)
			this.authorizedSubsets = new HashSet<>(authorizedSubsets);
		this.maxNbOfSubsets = maxNbOfSubsets;
	}

	@Override
	public List<List<List<T>>> getAllPartitions() {
		if (authorizedSubsets == null)
			return super.getAllPartitions();
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
				for (int i = 0; i < partitionEncoding.length; i++) {
					if (partitionEncoding[i] == subsetIdx)
						nextSubset.add(order.get(i));
				}
				if (!nextSubset.isEmpty() && !authorizedSubsets.contains(nextSubset))
					partitionIsValid = false;
				else {
					if (nextSubset.isEmpty()) {
						partitions.add(partition);
						noMoreSubsets = true;
					} else {
						partition.add(nextSubset);
						subsetIdx++;
					}
				}
			} while (!noMoreSubsets && partitionIsValid);
		} while (advance());
		return partitions;
	}

	@Override
	public List<List<Set<T>>> getAllPartitionsAsListsOfSets() {
		if (authorizedSubsets == null)
			return super.getAllPartitionsAsListsOfSets();
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
				for (int i = 0; i < partitionEncoding.length; i++) {
					if (partitionEncoding[i] == subsetIdx)
						nextSubset.add(order.get(i));
				}
				if (!nextSubset.isEmpty() && !authorizedSubsets.contains(nextSubset))
					partitionIsValid = false;
				else {
					if (nextSubset.isEmpty()) {
						partitions.add(partition);
						noMoreSubsets = true;
					} else {
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
		if (authorizedSubsets == null)
			return super.getNewPartitioner(set);
		List<List<T>> authorizedSubsets = new ArrayList<>();
		for (List<T> authorized : this.authorizedSubsets) {
			if (set.containsAll(authorized))
				authorizedSubsets.add(authorized);
		}
		return new ConstrainedPartitioner<>(order, authorizedSubsets, maxNbOfSubsets);
	}

	@Override
	protected IPartitioner<T> getNewPartitioner(List<T> setAsList) {
		if (authorizedSubsets == null)
			return super.getNewPartitioner(setAsList);
		List<List<T>> authorizedSubsets = new ArrayList<>();
		for (List<T> authorized : this.authorizedSubsets) {
			if (setAsList.containsAll(authorized))
				authorizedSubsets.add(authorized);
		}
		return new ConstrainedPartitioner<>(setAsList, authorizedSubsets, maxNbOfSubsets);
	}

	@Override
	protected int getIndexOfTheRightmostIncrementableElement() {
		if (maxNbOfSubsets == null)
			return super.getIndexOfTheRightmostIncrementableElement();
		// the first element is never incrementable
		for (int i = partitionEncoding.length - 1; i > 0; i--) {
			int iValue = partitionEncoding[i];
			if (iValue < maxNbOfSubsets && partitionPrefixContains(iValue, i)) {
				return i;
			}
		}
		return -1;
	}

}
