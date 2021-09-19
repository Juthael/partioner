package com.tregouet.partitioner.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.tregouet.partitioner.IPartitioner;

public class Partitioner<T> implements IPartitioner<T> {

	List<T> list;
	int[] partitionEncoding;
	int rightMostIncrementableIdx;
	
	public Partitioner(Set<T> set) {
		list = new ArrayList<>(set);
		partitionEncoding = new int[list.size()];
	}
	
	/**
	 * Unsafe : no unicity check
	 * @param set
	 */
	private Partitioner(List<T> set) {
		list = set;
		partitionEncoding = new int[list.size()];
	}

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
						nextSubset.add(list.get(i));
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

	public List<List<List<T>>> getAllRecursivePartitions() {
		List<List<List<T>>> recursivePartitions = new ArrayList<>();
		for (List<List<T>> partition : getAllPartitions()) {
			if (partition.size() == 1) {
				//then the list partition is the list itself, so recursion on it leads to an infinite loop
				recursivePartitions.add(partition);
			}
			else {
				/*
				 * LLLL   -	partitions for each subset
				 * [L]LLL -	partitions for i[th] subset
				 * L[L]LL -	j[th] partition for i[th] subset
				 * LL[L]L -	k[th] subsubset of j[th] partition for i[th] subset
				 * LLL[L] -	[th] element of k[th] subsubset of j[th] partition for i[th] subset  
				 */
				List<List<List<List<T>>>> partitionsForEachSubset = new ArrayList<>();
				for (List<T> subset : partition) {
					List<List<List<T>>> subSetPartitions = new Partitioner<T>(subset).getAllRecursivePartitions();
					partitionsForEachSubset.add(subSetPartitions);
				}
			}
			
			for(List<List<List<T>>> forEachSubsetOnePartition : Lists.cartesianProduct(partitionsForEachSubset)) {
				List<List<T>> recursivePartition = new ArrayList<>();
				for (List<List<T>> partitionForOneSubset : forEachSubsetOnePartition) {
					for (List<T> subsetOfASubset : partitionForOneSubset)
						recursivePartition.add(subsetOfASubset);
				}
				recursivePartitions.add(recursivePartition);	
			}
		}
		return recursivePartitions;
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
