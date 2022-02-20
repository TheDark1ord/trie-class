package Trie;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Rule;

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


        Trie test2 = new Trie();
        Assert.assertFalse(test2.searchString(""));
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void delete_string()
    {
        Trie test1 = new Trie();
        test1.addString("");
        test1.addString("Some String");
        test1.addString("Some");
        test1.addString("Another String");

        test1.deleteString("");
        exception.expect(RuntimeException.class);
        test1.deleteString("");

        test1.deleteString("Some String");
        Assert.assertTrue(test1.searchString("Some"));

        Trie test2 = new Trie();
        test2.addString("Some");
        test2.addString("Some String");

        test2.deleteString("Some");
        Assert.assertTrue(test2.searchString("Some String"));

        exception.expect(RuntimeException.class);
        test2.deleteString("Some S");
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
}
