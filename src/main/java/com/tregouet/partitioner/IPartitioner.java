package com.tregouet.partitioner;

import java.util.Set;

public interface IPartitioner<T> {
	
	Set<Set<T>> getAllSubsets(Set<T> set);
	
	Set<Set<T>> getAllSubsetsRecursively(Set<T> set);

}
