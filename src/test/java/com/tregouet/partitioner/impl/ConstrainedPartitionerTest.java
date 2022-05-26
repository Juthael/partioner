package com.tregouet.partitioner.impl;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Sets;

public class ConstrainedPartitionerTest {

	private static Set<Character> chars = new HashSet<>(
			Arrays.asList(new Character[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G' }));
	private static Set<Set<Character>> powerSet;
	private static Set<Set<Character>> forbiddenSubsets = new HashSet<>();
	private static Set<Set<Character>> authorizedSubsets;
	private static Integer maxSize = 4;
	private static Partitioner<Character> partitioner;
	private static List<List<List<Character>>> allPartitions;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		forbiddenSubsets.add((new HashSet<>(Arrays.asList(new Character[] { 'A', 'B', 'C' }))));
		forbiddenSubsets.add(new HashSet<>(Arrays.asList(new Character[] { 'D', 'E' })));
		powerSet = Sets.powerSet(chars);
		authorizedSubsets = new HashSet<>(Sets.difference(powerSet, forbiddenSubsets));
		partitioner = new Partitioner<>(chars);
		allPartitions = partitioner.getAllPartitions();
	}

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void whenConstrainedPartitionsReturnedThenSubsetOfPartitions() {
		ConstrainedPartitioner<Character> constrainedPartitioner = new ConstrainedPartitioner<>(chars,
				authorizedSubsets, maxSize);
		List<List<List<Character>>> constrainedPartitions = constrainedPartitioner.getAllPartitions();
		assertTrue(!constrainedPartitions.isEmpty() && allPartitions.containsAll(constrainedPartitions));
	}

	@Test
	public void whenPartitionNotContainedInSubsetConstrainedPartitionThenContainsForbiddenSubset() {
		boolean asExpected = true;
		ConstrainedPartitioner<Character> constrainedPartitioner = new ConstrainedPartitioner<>(chars,
				authorizedSubsets, null);
		List<List<List<Character>>> constrainedPartitions = constrainedPartitioner.getAllPartitions();
		List<List<List<Character>>> rejectedPartitions = new ArrayList<>();
		for (List<List<Character>> partition : allPartitions)
			if (!constrainedPartitions.contains(partition))
				rejectedPartitions.add(partition);
		for (List<List<Character>> rejected : rejectedPartitions) {
			Set<Set<Character>> rejectedSetOfSubsets = asSets(rejected);
			if (Sets.intersection(rejectedSetOfSubsets, forbiddenSubsets).isEmpty())
				asExpected = false;
		}
		assertTrue(!rejectedPartitions.isEmpty() && asExpected);
	}

	@Test
	public void whenPartitionNotContainedInSizeConstrainedPartitionThenHasForbiddenSize() {
		boolean asExpected = true;
		ConstrainedPartitioner<Character> constrainedPartitioner = new ConstrainedPartitioner<>(chars, null,
				maxSize);
		List<List<List<Character>>> constrainedPartitions = constrainedPartitioner.getAllPartitions();
		List<List<List<Character>>> rejectedPartitions = new ArrayList<>();
		for (List<List<Character>> partition : allPartitions)
			if (!constrainedPartitions.contains(partition))
				rejectedPartitions.add(partition);
		for (List<List<Character>> rejected : rejectedPartitions) {
			if (rejected.size() <= maxSize)
				asExpected = false;
			/*
			 * System.out.println(rejected);
			 */
		}
		assertTrue(!rejectedPartitions.isEmpty() && asExpected);
	}

	@Test
	public void whenPartitionNotContainedInConstrainedPartitionThenHasEitherForbiddenSubsetOrForbiddenSize() {
		boolean asExpected = true;
		ConstrainedPartitioner<Character> constrainedPartitioner = new ConstrainedPartitioner<>(chars,
				authorizedSubsets, maxSize);
		List<List<List<Character>>> constrainedPartitions = constrainedPartitioner.getAllPartitions();
		List<List<List<Character>>> rejectedPartitions = new ArrayList<>();
		for (List<List<Character>> partition : allPartitions)
			if (!constrainedPartitions.contains(partition))
				rejectedPartitions.add(partition);
		for (List<List<Character>> rejected : rejectedPartitions) {
			if (rejected.size() <= maxSize) {
				Set<Set<Character>> rejectedSetOfSubsets = asSets(rejected);
				if (Sets.intersection(rejectedSetOfSubsets, forbiddenSubsets).isEmpty())
					asExpected = false;
			}
			/*
			 * System.out.println(rejected);
			 */
		}
		assertTrue(!rejectedPartitions.isEmpty() && asExpected);
	}

	private Set<Set<Character>> asSets(List<List<Character>> lists) {
		Set<Set<Character>> setOfSets = new HashSet<>();
		for (List<Character> charList : lists) {
			Set<Character> charSet = new HashSet<>(charList);
			setOfSets.add(charSet);
		}
		return setOfSets;
	}

}
