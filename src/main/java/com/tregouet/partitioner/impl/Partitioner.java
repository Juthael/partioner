package com.tregouet.partitioner.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.tregouet.partitioner.IPartitioner;

public class Partitioner<T> implements IPartitioner<T> {

	List<T> list;
	List<Integer> partition;
	int rightMostIncrementableIdx;
	
	public Partitioner(Set<T> set) {
		list = new ArrayList<>(set);
		Integer[] partitionArray = new Integer[list.size()];
		Arrays.fill(partitionArray, 1);
		partition = Arrays.asList(partitionArray);
	}

	public Set<Set<T>> getAllSubsets(Set<T> set) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Set<T>> getAllSubsetsRecursively(Set<T> set) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void setIndexOfTheRightmostIncrementableElement(List<Integer> partition) {
		//the first element is never incrementable
		for (int i = partition.size() - 1 ; i > 0 ; i--) {
			Integer iValue = partition.get(i);
			if (partition.subList(0, i).contains(iValue)) {
				rightMostIncrementableIdx = i;
				break;
			}
		}
		rightMostIncrementableIdx = -1;
	}
	
	private void setSuffixStartingAfterSpecifiedIdxAtMinimalValue(int idx) {
		for (int i = idx + 1 ; i < partition.size() ; i++) {
			partition.add(i, 1);
		}
	}

}
