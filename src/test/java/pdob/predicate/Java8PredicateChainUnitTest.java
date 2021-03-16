package pdob.predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class Java8PredicateChainUnitTest {
    
    private List<String> names = Arrays.asList("Adam", "Alexander", "John", "Tom");
    private ArrayList<String> arrnames = new ArrayList<>(Arrays.asList("Adam", "Alexander", "John", "Tom"));

    @Test
    public void whenFilterList_thenSuccess() {
        List<String> result = names.stream()
            .filter(name -> name.startsWith("A"))
            .collect(Collectors.toList());

        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList("Adam", "Alexander")));
    }

    @Test
    public void whenFilterListWithMultipleFilters_thenSuccess() {
        List<String> result = names.stream()
            .filter(name -> name.startsWith("A"))
            .filter(name -> name.length() < 5)
            .collect(Collectors.toList());
        
        assertEquals(1, result.size());
        assertTrue(result.contains("Adam"));
       
    }

    @Test
    public void whenFilterListWithComplexPredicate_thenSuccess() {
        List<String> result = names.stream()
            .filter(name -> name.startsWith("A") && name.length() < 5)
            .collect(Collectors.toList());
        
        assertEquals(1, result.size());
        assertTrue(result.contains("Adam"));
    }

    @Test
    public void whenFilterListWithCombinedPredicatesInline_thenSuccess() {
        List<String> result = names.stream()
            .filter(((Predicate<String>) name -> name.startsWith("A")).and(name -> name.length() < 5))
            .collect(Collectors.toList());
        
        assertEquals(1, result.size());
        assertTrue(result.contains("Adam"));
    }

    @Test
    public void whenFilterListWithCombinedPredicatesUsingAnd_thenSuccess() {
        Predicate<String> predicate1 = str -> str.startsWith("A");
        Predicate<String> predicate2 = str -> str.length() < 5;

        List<String> result = names.stream()
            .filter(predicate1.and(predicate2))
            .collect(Collectors.toList());

        assertEquals(1, result.size());
        assertTrue(result.contains("Adam"));
    }

    @Test
    public void whenFilterListWithCombinedPredicatesUsingOr_thenSuccess() {
        Predicate<String> predicate1 = str -> str.startsWith("J");
        Predicate<String> predicate2 = str -> str.length() < 4;
        

        List<String> result = names.stream()
            .filter(predicate1.or(predicate2))
            .collect(Collectors.toList());

        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList("John", "Tom")));
    }

    @Test
    public void whenFilterListWithCombinedPredicatesUsingOrAndNegate_thenSuccess() {
        Predicate<String> predicate1 = str -> str.startsWith("J");
        Predicate<String> predicate2 = str -> str.length() < 4;

        List<String> result = names.stream()
            .filter(predicate1.or(predicate2.negate()))
            .collect(Collectors.toList());

        assertEquals(3, result.size());
        assertTrue(result.containsAll(Arrays.asList("Adam", "Alexander", "John")));
    }

    @Test
    public void whenFilterListWithCollectionOfPredicatesUsingAnd_thenSuccess() {
        List<Predicate<String>> allPredicates = new ArrayList<Predicate<String>>();
        allPredicates.add(str -> str.startsWith("A"));
        allPredicates.add(str -> str.contains("d"));
        allPredicates.add(str -> str.length() > 4);

        List<String> result = names.stream()
            .filter(allPredicates.stream()
                .reduce(x -> true, Predicate::and))
            .collect(Collectors.toList());

        assertEquals(1, result.size());
        assertTrue(result.contains("Alexander"));
    }

    @Test
    public void whenFilterListWithCollectionOfPredicatesUsingOr_thenSuccess() {
        List<Predicate<String>> allPredicates = new ArrayList<Predicate<String>>();
        allPredicates.add(str -> str.startsWith("A"));
        allPredicates.add(str -> str.contains("d"));
        allPredicates.add(str -> str.length() > 4);

        List<String> result = names.stream()
            .filter(allPredicates.stream()
                .reduce(x -> false, Predicate::or))
            .collect(Collectors.toList());

        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList("Adam", "Alexander")));
    }
    
    @Test
    public void filterArrayList3argsPredicatesUsingOr_3flagstrue_thenSuccess() {
        //pass for 3 flags true : 
        //boolean AdamFlag = true
        //boolean AlexanderFlag = true
        //boolean JohnFlag = true
        
        ArrayList<String> actual = new ArrayList<String>();
        actual =filterArrayList3args(arrnames,true, true, true);
        ArrayList<String> expected = new ArrayList<>(Arrays.asList("Adam", "Alexander", "John"));
        
        assertTrue(actual.containsAll(expected));
        
        System.out.println("Actual 3 args");
        System.out.println(actual);
    }
    
     @Test
    public void filterArrayList3argsPredicatesUsingOr_2flagstrue_thenSuccess() {
        //pass for 2 flags true
        //boolean AdamFlag = true
        //boolean AlexanderFlag = true
        //boolean JohnFlag = false
        
        ArrayList<String> actual = new ArrayList<String>();
        actual =filterArrayList3args(arrnames,true, true, false);
        ArrayList<String> expected = new ArrayList<>(Arrays.asList("Adam", "Alexander"));
        
        assertTrue(actual.containsAll(expected));
        
        System.out.println("Actual 2 args");
        System.out.println(actual);
    }
    
    @Test
    public void filterArrayList3argsPredicatesUsingOr_1flagstrue_thenSuccess() {
        //pass for 1 flag true
        //boolean AdamFlag = true
        //boolean AlexanderFlag = false
        //boolean JohnFlag = false
        
        ArrayList<String> actual = new ArrayList<String>();
        actual =filterArrayList3args(arrnames, true, false, false);
        ArrayList<String> expected = new ArrayList<>(Arrays.asList("Adam"));
        
        assertTrue(actual.containsAll(expected));
        
        System.out.println("Actual 1 arg");
        System.out.println(actual);
    }
    
    @Test
    public void filterArrayList3argsPredicatesUsingOr_0flagstrue_thenSuccess() {
        //pass for 0 flag true
        //boolean AdamFlag = false
        //boolean AlexanderFlag = false
        //boolean JohnFlag = false
        
        ArrayList<String> actual = new ArrayList<String>();
        actual =filterArrayList3args(arrnames, false, false, false);
        
        System.out.println("Actual 0 arg");
        System.out.println(actual);

        assertTrue(actual.isEmpty());
   
    }
    

    public ArrayList<String> filterArrayList3args (ArrayList<String> inputArrayList, boolean AdamFlag, boolean AlexanderFlag, boolean JohnFlag){
        
        Predicate<String> predicate1 = str -> false;
        Predicate<String> predicate2 = str -> false;
        Predicate<String> predicate3 = str -> false;
        
        if (AdamFlag){
            predicate1 = str -> str.equals("Adam");
        }
        
        if (AlexanderFlag){
            predicate2 = str -> str.equals("Alexander");
        }
         
        if (JohnFlag){
            predicate3 = str -> str.equals("John");
        }
            
        
        Predicate<String> combinedpredicate = predicate1.or(predicate2).or(predicate3);
        
         List<String> resultlist = inputArrayList.stream()
            .filter(combinedpredicate).collect(Collectors.toList());
         
         ArrayList<String> result = new ArrayList<>(resultlist);
        
        return result;
    }
}
