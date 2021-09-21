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

import com.tregouet.partitioner.IPartitioner;

public class PartitionerTest {

	private static Set<Character> chars = new HashSet<>(Arrays.asList(new Character[] {'A', 'B', 'C', 'D'}));
	private static Set<Character> moreChars = 
			new HashSet<>(Arrays.asList(new Character[] {'A', 'B', 'C', 'D', 'E', 'F', 'G'}));
	private static IPartitioner<Character> partitioner;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void whenPartitionsRequestedThenExpectedReturned() {
		partitioner = new Partitioner<>(chars);
		List<List<List<Character>>> partitions = partitioner.getAllPartitions();
		/*
		int idx = 1;
		for (List<List<Character>> partition : partitions) {	
			System.out.print("Partition " + Integer.toString(idx) + " : ");
			System.out.println(partition.toString());
			idx++;
			}
		*/
		assertTrue(partitions.equals(partitionsObtainedByManualExecutionOfTheAlgorithm()));
	}
	
	
	@Test
	public void whenHierarchiesReturnedThenReallyAreSpanningHierarchies() {
		boolean properHierarchies = true;
		boolean containsUnionAndAtoms = true;
		boolean everyHierarchyIsGeneratedOnce = true;
		
		Character[] sortedSetArray = moreChars.toArray(new Character[moreChars.size()]);
		Arrays.sort(sortedSetArray);
		List<Character> sortedList = Arrays.asList(sortedSetArray);
		partitioner = new Partitioner<>(moreChars);
		
		List<List<List<Character>>> hierarchies = partitioner.getAllSpanningHierarchiesAsListsOfLists();
		Set<List<List<Character>>> hierarchiesAsSet = new HashSet<>();
		/*
		int hierarchyIdx = 1;
		for (List<List<Character>> hierarchy : hierarchies) {
			System.out.print("Hierarchy " + Integer.toString(hierarchyIdx) + " : ");
			System.out.println(hierarchy.toString());
			hierarchyIdx++;
		}
		*/
		for (List<List<Character>> hierarchy : hierarchies) {
			if (!containsUnionAndAtoms(sortedList, hierarchy))
				containsUnionAndAtoms = false;
			if (!hierarchiesAsSet.add(hierarchy))
				everyHierarchyIsGeneratedOnce = false;
			for (int i = 0 ; i < hierarchy.size() - 1 ; i++) {
				for (int j = i+1 ; j < hierarchy.size() ; j++) {
					List<Character> subsetA = new ArrayList<>(hierarchy.get(i));
					List<Character> subsetB = new ArrayList<>(hierarchy.get(j));
					if (subsetA.containsAll(subsetB)
							|| subsetB.containsAll(subsetA)
							|| !subsetA.removeAll(subsetB)){
								//then ok
							}
					else properHierarchies = false;
				}
			}

		}	
		assertTrue(containsUnionAndAtoms && properHierarchies && everyHierarchyIsGeneratedOnce);
	}
	
	private List<List<List<Character>>> partitionsObtainedByManualExecutionOfTheAlgorithm(){
		List<List<List<Character>>> partitions = new ArrayList<>();
		List<List<Character>> partition1 = new ArrayList<>();
		List<List<Character>> partition2 = new ArrayList<>();
		List<List<Character>> partition3 = new ArrayList<>();
		List<List<Character>> partition4 = new ArrayList<>();
		List<List<Character>> partition5 = new ArrayList<>();
		List<List<Character>> partition6 = new ArrayList<>();
		List<List<Character>> partition7 = new ArrayList<>();
		List<List<Character>> partition8 = new ArrayList<>();
		List<List<Character>> partition9 = new ArrayList<>();
		List<List<Character>> partition10 = new ArrayList<>();
		List<List<Character>> partition11 = new ArrayList<>();
		List<List<Character>> partition12 = new ArrayList<>();
		List<List<Character>> partition13 = new ArrayList<>();
		List<List<Character>> partition14 = new ArrayList<>();
		List<List<Character>> partition15 = new ArrayList<>();
		
		partition1.add(new ArrayList<>(Arrays.asList(new Character[] {'A', 'B', 'C', 'D'})));
		
		partition2.add(new ArrayList<>(Arrays.asList(new Character[] {'A', 'B', 'C'})));
		partition2.add(new ArrayList<>(Arrays.asList(new Character[] {'D'})));
		
		partition3.add(new ArrayList<>(Arrays.asList(new Character[] {'A', 'B', 'D'})));
		partition3.add(new ArrayList<>(Arrays.asList(new Character[] {'C'})));
		
		partition4.add(new ArrayList<>(Arrays.asList(new Character[] {'A', 'B'})));
		partition4.add(new ArrayList<>(Arrays.asList(new Character[] {'C', 'D'})));
		
		partition5.add(new ArrayList<>(Arrays.asList(new Character[] {'A', 'B'})));
		partition5.add(new ArrayList<>(Arrays.asList(new Character[] {'C'})));
		partition5.add(new ArrayList<>(Arrays.asList(new Character[] {'D'})));
		
		partition6.add(new ArrayList<>(Arrays.asList(new Character[] {'A', 'C', 'D'})));
		partition6.add(new ArrayList<>(Arrays.asList(new Character[] {'B'})));
		
		partition7.add(new ArrayList<>(Arrays.asList(new Character[] {'A', 'C'})));
		partition7.add(new ArrayList<>(Arrays.asList(new Character[] {'B', 'D'})));
		
		partition8.add(new ArrayList<>(Arrays.asList(new Character[] {'A', 'C'})));
		partition8.add(new ArrayList<>(Arrays.asList(new Character[] {'B'})));
		partition8.add(new ArrayList<>(Arrays.asList(new Character[] {'D'})));
		
		partition9.add(new ArrayList<>(Arrays.asList(new Character[] {'A', 'D'})));
		partition9.add(new ArrayList<>(Arrays.asList(new Character[] {'B', 'C'})));
		
		partition10.add(new ArrayList<>(Arrays.asList(new Character[] {'A'})));
		partition10.add(new ArrayList<>(Arrays.asList(new Character[] {'B', 'C', 'D'})));
		
		partition11.add(new ArrayList<>(Arrays.asList(new Character[] {'A'})));
		partition11.add(new ArrayList<>(Arrays.asList(new Character[] {'B', 'C'})));
		partition11.add(new ArrayList<>(Arrays.asList(new Character[] {'D'})));
		
		partition12.add(new ArrayList<>(Arrays.asList(new Character[] {'A', 'D'})));
		partition12.add(new ArrayList<>(Arrays.asList(new Character[] {'B'})));
		partition12.add(new ArrayList<>(Arrays.asList(new Character[] {'C'})));
		
		partition13.add(new ArrayList<>(Arrays.asList(new Character[] {'A'})));
		partition13.add(new ArrayList<>(Arrays.asList(new Character[] {'B', 'D'})));
		partition13.add(new ArrayList<>(Arrays.asList(new Character[] {'C'})));
		
		partition14.add(new ArrayList<>(Arrays.asList(new Character[] {'A'})));
		partition14.add(new ArrayList<>(Arrays.asList(new Character[] {'B'})));
		partition14.add(new ArrayList<>(Arrays.asList(new Character[] {'C', 'D'})));
		
		partition15.add(new ArrayList<>(Arrays.asList(new Character[] {'A'})));
		partition15.add(new ArrayList<>(Arrays.asList(new Character[] {'B'})));
		partition15.add(new ArrayList<>(Arrays.asList(new Character[] {'C'})));
		partition15.add(new ArrayList<>(Arrays.asList(new Character[] {'D'})));	
		
		partitions.add(partition1);
		partitions.add(partition2);
		partitions.add(partition3);
		partitions.add(partition4);
		partitions.add(partition5);
		partitions.add(partition6);
		partitions.add(partition7);
		partitions.add(partition8);
		partitions.add(partition9);
		partitions.add(partition10);
		partitions.add(partition11);
		partitions.add(partition12);
		partitions.add(partition13);
		partitions.add(partition14);
		partitions.add(partition15);
		
		return partitions;
	}
	
	private boolean containsUnionAndAtoms(List<Character> set, List<List<Character>> hierarchy) {
		if (!hierarchy.contains(set))
			return false;
		for (Character character : set) {
			if (!hierarchy.contains(Arrays.asList(new Character[] {character})))
				return false;
		}
		return true;
	}

}
