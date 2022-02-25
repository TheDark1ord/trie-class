package Trie;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.Assert;
import org.apache.commons.lang3.RandomStringUtils;

public class TrieTests
{
    @Test
    public void search_string()
    {
        Trie test1 = new Trie();
        test1.addString("Hello world!");
        test1.addString("Hello");
        // I assume this is a valid input
        test1.addString("");

        Assert.assertTrue(test1.searchString("Hello world!"));
        Assert.assertTrue(test1.searchString("Hello"));
        Assert.assertTrue(test1.searchString(""));

        Assert.assertFalse(test1.searchString("Hello world"));
        Assert.assertFalse(test1.searchString("hello world!"));
        Assert.assertFalse(test1.searchString("H"));
        Assert.assertFalse(test1.searchString("Hg"));
        Assert.assertFalse(test1.searchString("G"));
    }

    @Test
    public void delete_string()
    {
        Trie test1 = new Trie();
        test1.addString("");
        test1.addString("Some String");
        test1.addString("Some");
        test1.addString("Another String");

        Assert.assertTrue(test1.searchString("Another String"));
        test1.deleteString("Another String");

        test1.deleteString("");
        assertThrows((arg) -> test1.deleteString(arg), "", RuntimeException.class);

        test1.deleteString("Some String");
        Assert.assertTrue(test1.searchString("Some"));

        test1.deleteString("Some");


        Trie test2 = new Trie();
        test2.addString("Some");
        test2.addString("Some String");

        test2.deleteString("Some");
        Assert.assertTrue(test2.searchString("Some String"));
        test2.deleteString("Some String");

        assertThrows((arg) -> test2.deleteString(arg), "Some S", RuntimeException.class);
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
        Assert.assertTrue(tha_res_exp.equals(tha_res_act));

        List<String> the_res_act = test1.searchPrefix("the");
        Assert.assertTrue(the_res_exp.equals(the_res_act));

        List<String> th_res_exp = Stream.concat(
            tha_res_exp.stream(),
             the_res_exp.stream()
             ).toList();
        List<String> th_res_act = test1.searchPrefix("th");
        Assert.assertTrue(th_res_exp.equals(th_res_act));

        List<String> all_res_act = test1.searchPrefix("");
        Assert.assertTrue(th_res_exp.equals(all_res_act));
    }

    @Test
    public void randomTests()
    {
        Trie addStringTest = new Trie();
        List<String> addedStrings = new ArrayList<String>();

        for (int i = 0; i < 1000; i++)
        {
            addedStrings.add(RandomStringUtils.randomAlphabetic(1000));
            addStringTest.addString(addedStrings.get(i));
        }
        for (int i = 0; i < 1000; i++)
        {
            Assert.assertTrue(addStringTest.searchString(addedStrings.get(i)));
        }
        
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
           + arg
            + " param",
             pass);
    }
}
