package Trie;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.Assert;
import org.apache.commons.lang3.RandomStringUtils;

public class TrieTests
{
    @Test
    public void search_string()
    {
        Trie test1 = new Trie(new String[]{
            "", "Some String", "Some String",
        });
        test1.addString("some string");
        test1.addString("some");

        Assert.assertTrue(test1.containsString(""));
        Assert.assertTrue(test1.containsString("Some String"));
        Assert.assertTrue(test1.containsString("some string"));
        Assert.assertTrue(test1.containsString("some"));

        Assert.assertFalse(test1.containsString("Some"));
        Assert.assertFalse(test1.containsString("some strin"));
        Assert.assertFalse(test1.containsString("Some strin"));

        Trie test2 = new Trie();
        HashSet<String> addedStrings = new HashSet<String>();

        for (int i = 0; i < 50; i++)
        {
            String newString = RandomStringUtils.randomAlphanumeric(1);
            addedStrings.add(newString);
            test2.addString(newString);
        }
        for (int i = 0; i < 50; i++)
        {
            String newString = RandomStringUtils.randomAlphanumeric(2);
            addedStrings.add(newString);
            test2.addString(newString);
        }
        for (int i = 0; i < 50; i++)
        {
            String newString = RandomStringUtils.randomAlphanumeric(20);
            addedStrings.add(newString);
            test2.addString(newString);
        }

        Assert.assertFalse(test2.containsString(""));
        for (String str: addedStrings)
            Assert.assertTrue(test2.containsString(str));
    }

    @Test
    public void delete_string()
    {
        Trie test1 = new Trie(new String[]{
            "", "some", "Some", "Some string",
            "Anoter string", "Another string"
        });

        test1.deleteString("");
        assertThrows((String str) -> test1.deleteString(str), "", RuntimeException.class);
        Assert.assertFalse(test1.containsString(""));
        Assert.assertTrue(test1.containsString("some"));
        Assert.assertTrue(test1.containsString("Some"));
        Assert.assertTrue(test1.containsString("Some string"));
        Assert.assertTrue(test1.containsString("Another string"));

        test1.deleteString("Some");
        assertThrows((String str) -> test1.deleteString(str), "Some", RuntimeException.class);
        Assert.assertFalse(test1.containsString("Some"));
        Assert.assertTrue(test1.containsString("some"));
        Assert.assertTrue(test1.containsString("Some string"));

        test1.deleteString("Another string");
        assertThrows((String str) -> test1.deleteString(str), "Another string", RuntimeException.class);


        Trie test2 = new Trie();
        // Ensure, that there is no same values
        HashSet<String> addedStrings = new HashSet<String>();
        List<String> deletedStrings = new ArrayList<String>();

        for (int i = 0; i < 100; i++)
        {
            String newString = RandomStringUtils.randomAlphanumeric(20);
            addedStrings.add(newString);
            test2.addString(newString);
        }
        List<String> allStrings = test2.searchPrefix("");
        for (String i: allStrings)
            System.out.println(i);

        List<String> remainingStrings = new ArrayList<>(addedStrings);
        Random rand = new Random();
        for (int i = 0; i < 50; i++)
        {
            int randomIndex = rand.nextInt(remainingStrings.size() - 1);
            
            System.out.println("Deleted " + remainingStrings.get(randomIndex));
            try
            {
                test2.deleteString(remainingStrings.get(randomIndex));
            }
            catch (RuntimeException e)
            {
                test2.deleteString(remainingStrings.get(randomIndex));
            }

            deletedStrings.add(remainingStrings.remove(randomIndex));
        }

        for (String str: remainingStrings)
            Assert.assertTrue(test2.containsString(str));
        for (String str: deletedStrings)
        {
            Assert.assertFalse(test2.containsString(str));
            assertThrows((String arg) -> test1.deleteString(arg), str, RuntimeException.class);
        }
   }

    @Test
    public void search_prefix()
    {
        Trie test1 = new Trie();

        List<String> tha_res_exp = List.of(
            "tha", "thalami", "thalamocortical",
             "thalamocortically", "thalamostriate"
        );
        for (String word: tha_res_exp)
            test1.addString(word);
            
        List<String> the_res_exp = List.of(
            "the", "theacrine", "theacrines",
            "thearch", "thearchal"
        );    
        for (String word: the_res_exp)
            test1.addString(word);


        List<String> tha_res_act = test1.searchPrefix("tha");
        java.util.Collections.sort(tha_res_act);
        Assert.assertTrue(tha_res_exp.equals(tha_res_act));

        List<String> the_res_act = test1.searchPrefix("the");
        java.util.Collections.sort(the_res_act);
        Assert.assertTrue(the_res_exp.equals(the_res_act));

        List<String> th_res_exp = Stream.concat(
            tha_res_exp.stream(),
             the_res_exp.stream()
             ).toList();
        List<String> th_res_act = test1.searchPrefix("th");
        java.util.Collections.sort(th_res_act);
        Assert.assertTrue(th_res_exp.equals(th_res_act));

        List<String> all_res_act = test1.searchPrefix("");
        java.util.Collections.sort(all_res_act);
        Assert.assertTrue(th_res_exp.equals(all_res_act));
    }

    private void assertThrows(Consumer<String> func, String arg, Class<RuntimeException> expectedExceptionClass)
    {
        boolean pass = false;
        try {
            func.accept(arg);
        }
        catch (Exception e) {
            pass = e.getClass() == expectedExceptionClass;
        }
        Assert.assertTrue("Specified function failed to throw "
         + expectedExceptionClass
          + " when provided with "
           + arg,
             pass);
    }
}
