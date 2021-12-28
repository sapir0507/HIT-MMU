package com.hit.Algorithm;
import org.junit.Test;
import com.hit.Algorithm.LRUAlgoCacheImpl;
import com.hit.Algorithm.RandomAlgoCacheImpl;
import com.hit.Algorithm.SecondChance;


import static org.junit.Assert.assertEquals;

public class IAlgoCacheTest {

	private Integer[] keyList = new Integer[] {1,2,3,4,5,6};
	private String[] valueList = new String[] {"ab","bc","cd","de","ef","fg"};
	int size = 4;
	
	@Test
	public void testLRUAlgoCacheImpl()
	{
		//Create test
		LRUAlgoCacheImpl<Integer,String> testLRU = new LRUAlgoCacheImpl<>(size);
		assertEquals(testLRU.getCapacity(),size);
		
		//test put element
	
		testLRU.putElement(keyList[0], valueList[0]);
		testLRU.putElement(keyList[1], valueList[1]);
		testLRU.putElement(keyList[2], valueList[2]);
		testLRU.putElement(keyList[3], valueList[3]);
		testLRU.putElement(keyList[4], valueList[4]);
		testLRU.putElement(keyList[5], valueList[5]);

			
		//map should look like [5, "ef"]  [6, "fg"] [3, "cd"] [4, "de"] 
		//test get element
	
		assertEquals(testLRU.getElement(keyList[4]),testLRU.getElement(5));
		assertEquals(testLRU.getElement(keyList[5]),testLRU.getElement(6));
		assertEquals(testLRU.getElement(keyList[2]),testLRU.getElement(3));
		assertEquals(testLRU.getElement(keyList[3]),testLRU.getElement(4));
		
		//check for key not present
		assertEquals(testLRU.getElement(keyList[0]),null);
		assertEquals(testLRU.getElement(keyList[1]),null);
		
		
		//test remove element
		testLRU.removeElement(4);
		assertEquals(testLRU.getElement(4),null);
} 

	
	@Test
	public void testRandomAlgoCacheImpl()
	{

		RandomAlgoCacheImpl<Integer,String> testRandom = new RandomAlgoCacheImpl<>(size);
		assertEquals(testRandom.getCapacity(),size);
		
		//test put element
	
		testRandom.putElement(keyList[0], valueList[0]);
		testRandom.putElement(keyList[1], valueList[1]);
		testRandom.putElement(keyList[2], valueList[2]);
		testRandom.putElement(keyList[3], valueList[3]);


		//test remove element
		testRandom.removeElement(1);
		assertEquals(testRandom.getElement(1),null);			

	}

    @Test
    public void testSecondChance()
    {
		SecondChance<Integer,String> testSecondChance = new SecondChance<>(size);
		assertEquals(testSecondChance.getCapacity(),size);

		//test put element
		testSecondChance.putElement(keyList[0], valueList[0]); //1
		testSecondChance.putElement(keyList[1], valueList[1]); //2
		testSecondChance.putElement(keyList[2], valueList[2]); //3
		testSecondChance.putElement(keyList[3], valueList[3]); //4
		
		//test get element		
		testSecondChance.getElement(1); //marks 1
		testSecondChance.getElement(2);	//marks 2

		testSecondChance.putElement(keyList[5], valueList[5]); //6
		testSecondChance.putElement(keyList[2], valueList[2]); //3
		testSecondChance.putElement(keyList[0], valueList[0]); //1
		testSecondChance.putElement(keyList[0], valueList[2]); //1
		
		// map will contain keys 1, 2, 6, 3
		
		assertEquals(testSecondChance.getElement(keyList[0]),testSecondChance.getElement(1));
		assertEquals(testSecondChance.getElement(keyList[1]),testSecondChance.getElement(2));
		assertEquals(testSecondChance.getElement(keyList[5]),testSecondChance.getElement(6));	
		assertEquals(testSecondChance.getElement(keyList[2]),testSecondChance.getElement(3));			
    }
}