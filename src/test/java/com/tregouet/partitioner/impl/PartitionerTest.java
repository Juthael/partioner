package com.tregouet.partitioner.impl;

import static org.junit.Assert.*;

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
	private static IPartitioner<Character> partitioner = new Partitioner<>(chars);
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void whenPartitionsRequestedThenExpectedReturned() {
		for (List<List<Character>> partition : partitioner.getAllPartitions()) {
			System.out.println("***New Partition***");
			for (List<Character> subset : partition) {
				System.out.print(subset);
			}
			System.out.println(System.lineSeparator());
		}
		
	}
	
	@Test
	public void whenRecursivePartitionsRequestedThenExpectedReturned() {
		for (List<List<Character>> partition : partitioner.getAllRecursivePartitions()) {
			System.out.println("***New Partition***");
			for (List<Character> subset : partition) {
				System.out.print(subset);
			}
			System.out.println(System.lineSeparator());
		}
		
	}	

}
