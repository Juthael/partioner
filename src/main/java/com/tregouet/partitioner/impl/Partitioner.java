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
		Arrays.fill(partitionEncoding, 1);
	}
	
	/**
	 * Unsafe : no unicity check
	 * @param set
	 */
	private Partitioner(List<T> set) {
		list = set;
		partitionEncoding = new int[list.size()];
		Arrays.fill(partitionEncoding, 1);
	}

	public List<List<List<T>>> getAllPartitions() {
		List<List<List<T>>> partitions = new ArrayList<>();
		do {
			List<List<T>> partition = new ArrayList<>();
			boolean noMoreSubsets = false;
			int subsetIdx = 1;
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
			} while (noMoreSubsets);			
		} while (advance());
		return partitions;
	}

	public List<List<List<T>>> getSubsetsOfAllRecursivePartitions() {
		List<List<List<T>>> recursivePartitions = new ArrayList<>();
		for (List<List<T>> partition : getAllPartitions()) {
			List<List<List<List<T>>>> partitionsForEachSubset = new ArrayList<>();
			for (List<T> subset : partition) {
				List<List<List<T>>> subSetPartitions = new Partitioner<T>(subset).getSubsetsOfAllRecursivePartitions();
				partitionsForEachSubset.add(subSetPartitions);
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
		setIndexOfTheRightmostIncrementableElement();
		if (rightMostIncrementableIdx == -1)
			return false;
		else {
			partitionEncoding[rightMostIncrementableIdx]++;
			setSuffixAtMinimalValue(rightMostIncrementableIdx);
			return true;
		}
	}
	
	private void setIndexOfTheRightmostIncrementableElement() {
		//the first element is never incrementable
		for (int i = partitionEncoding.length - 1 ; i > 0 ; i--) {
			int iValue = partitionEncoding[i];
			if (partitionPrefixContains(iValue, i)) {
				rightMostIncrementableIdx = i;
				break;
			}
		}
		rightMostIncrementableIdx = -1;
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
			partitionEncoding[i] = 1;
		}
	}

}
