package com.tregouet.partitioner.impl;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
	public void whenHierarchiesRequestedThenReallyAreSpanningHierarchies() {
		boolean spanning = true;
		boolean properHierarchies = true;
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
				spanning = false;
			if (!hierarchiesAsSet.add(hierarchy))
				everyHierarchyIsGeneratedOnce = false;
			for (int i = 0 ; i < hierarchy.size() - 1 ; i++) {
				for (int j = i+1 ; j < hierarchy.size() ; j++) {
					List<Character> subsetA = new ArrayList<>(hierarchy.get(i));
					List<Character> subsetB = new ArrayList<>(hierarchy.get(j));
					if (!eitherOneSubsetIncludesTheOtherOrTheirIntersectionIsEmpty(subsetA, subsetB)){
						properHierarchies = false;
					}
				}
			}

		}	
		assertTrue(spanning && properHierarchies && everyHierarchyIsGeneratedOnce);
	}
	
	@Test
	public void whenHierarchiesRequestedThenExpectedReturned() {
		partitioner = new Partitioner<>(chars);
		Set<Set<Set<Character>>> returned = getHierarchiesAsSets(partitioner.getAllSpanningHierarchiesAsListsOfLists());
		Set<Set<Set<Character>>> expected = buildHierarchiesByDumbBruteForce(chars);
		assertTrue(returned.equals(expected));
	}
	
	private boolean eitherOneSubsetIncludesTheOtherOrTheirIntersectionIsEmpty(
			List<Character> subsetA, List<Character> subsetB) {
		if (subsetA.containsAll(subsetB)
				|| subsetB.containsAll(subsetA)
				|| !new ArrayList<>(subsetA).removeAll(subsetB))
			return true;
		return false;
	}
	
	private boolean eitherOneSubsetIncludesTheOtherOrTheirIntersectionIsEmpty(
			Set<Character> subsetA, Set<Character> subsetB) {
		if (subsetA.containsAll(subsetB)
				|| subsetB.containsAll(subsetA)
				|| !new ArrayList<>(subsetA).removeAll(subsetB))
			return true;
		return false;
	}	
	
	private boolean forEveryPairOfSubsetThenEitherOneIncludesTheOtherOrTheirIntersectionIsEmpty(
			Set<Set<Character>> hierarchy) {
		Iterator<Set<Character>> ite1 = hierarchy.iterator();
		Set<Set<Character>> alreadyChecked = new HashSet<>();
		while (ite1.hasNext()) {
			Set<Character> subsetA = ite1.next();
			Iterator<Set<Character>> ite2 = hierarchy.iterator();
			while (ite2.hasNext()) {
				Set<Character> subsetB = ite2.next();
				if (!alreadyChecked.contains(subsetB)) {
					if (!eitherOneSubsetIncludesTheOtherOrTheirIntersectionIsEmpty(subsetA, subsetB))
						return false;
				}	
			}
			alreadyChecked.add(subsetA);
		}
		return true;
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
	
	private boolean containsUnionAndAtoms(List<Character> unionOfAtoms, List<List<Character>> hierarchy) {
		if (!hierarchy.contains(unionOfAtoms))
			return false;
		for (Character atom : unionOfAtoms) {
			if (!hierarchy.contains(Arrays.asList(new Character[] {atom})))
				return false;
		}
		return true;
	}
	
	private boolean containsUnionAndAtoms(Set<Character> unionOfAtoms, Set<Set<Character>> hierarchy) {
		if (!hierarchy.contains(unionOfAtoms))
			return false;
		for (Character atom : unionOfAtoms) {
			if (!hierarchy.contains(new HashSet<>(Arrays.asList(new Character[] {atom}))))
				return false;
		}
		return true;
	}
	
	private Set<Set<Set<Character>>> getHierarchiesAsSets(List<List<List<Character>>> hierarchiesAsLists) {
		Set<Set<Set<Character>>> hierarchies = new HashSet<>();
		for (List<List<Character>> hierarchyAsList : hierarchiesAsLists) {
			Set<Set<Character>> hierarchy = new HashSet<>();
			for (List<Character> subsetAsList : hierarchyAsList)
				hierarchy.add(new HashSet<>(subsetAsList));
			hierarchies.add(hierarchy);
		}
		return hierarchies;
	}
	
	private Set<Set<Set<Character>>> buildHierarchiesByDumbBruteForce(Set<Character> atoms) {
		Set<Set<Set<Character>>> powerSetOfSubsets = buildPowerSetOfSubsets(buildPowerSetOfCharacters(atoms));
		Set<Set<Set<Character>>> hierarchies =  powerSetOfSubsets.stream()
				.filter(h -> containsUnionAndAtoms(atoms, h) 
						&& forEveryPairOfSubsetThenEitherOneIncludesTheOtherOrTheirIntersectionIsEmpty(h))
				.collect(Collectors.toSet());
		return hierarchies;
	}
	
	private Set<Set<Set<Character>>> buildPowerSetOfSubsets(Set<Set<Character>> powerSet) {
		List<Set<Character>> subsetList = new ArrayList<>(powerSet);
		subsetList.remove(new HashSet<Character>());
		Set<Set<Set<Character>>> powerSetOfSubsets = new HashSet<>();
		for (int i = 0 ; i < (1 << subsetList.size()) ; i++) {
			Set<Set<Character>> setOfSubsets = new HashSet<>();
			for (int j = 0 ; j < subsetList.size() ; j++) {
				if (((1 << j) & i) > 0)
					setOfSubsets.add(subsetList.get(j));
			}
			powerSetOfSubsets.add(setOfSubsets);
		}
		return powerSetOfSubsets;
	}
	
	private Set<Set<Character>> buildPowerSetOfCharacters(Set<Character> characters) {
		List<Character> charList = new ArrayList<>(characters);
	    Set<Set<Character>> powerSet = new HashSet<Set<Character>>();
	    for (int i = 0; i < (1 << charList.size()); i++) {
	    	Set<Character> subset = new HashSet<Character>();
	        for (int j = 0; j < charList.size(); j++) {
	            if(((1 << j) & i) > 0)
	            	subset.add(charList.get(j));
	        }
	        powerSet.add(subset);
	    }
	    return powerSet;
	}

}
