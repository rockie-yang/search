package search

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

/**
 * Created by : Rockie Yang(eyouyan@gmail.com, snowriver.org)
 * Created at : 2/12/13
 */
@RunWith(classOf[JUnitRunner])
class StringMatcherTest extends FunSuite {
  test("contain test for one string") {
    val matcher = StringMatcher("contain", caseSensitive = false, List("b"))
    assert(matcher("abc"), "abc should contain b")
    assert(matcher("ABC"), "ABC should contain b since it's case insensitive")
    assert(!matcher("acd"), "acd should not contain b")
  }

  test("contain ignore case test for one string") {
    val matcher = StringMatcher("contain", caseSensitive = true, List("b"))
    assert(matcher("abc"), "abc should contain b")
    assert(!matcher("ABC"), "ABC should not contain b since it's case sensitive")
    assert(!matcher("acd"), "acd should not contain b")
  }

  test("contain test for 2 strings") {
    val matcher = StringMatcher("contain", caseSensitive = false, List("b", "c"))
    assert(matcher("abc"), "abc should contain both b and c")
    assert(matcher("ABC"), "ABC should contain both b and c since it's case insensitive")
    assert(!matcher("acd"), "acd should not contain both b and c")
  }

  test("contain ignore case test for 2 strings") {
    val matcher = StringMatcher("contain", caseSensitive = true, List("b", "c"))
    assert(matcher("abc"), "abc should contain both b and c")
    assert(!matcher("ABC"), "ABC should contain both b and c since it's case sensitive")
    assert(!matcher("acd"), "acd should not contain both b and c")
  }

  test("un-contain test for one string") {
    val matcher = StringMatcher("un-contain", caseSensitive = false, List("b"))
    assert(!matcher("abc"), "abc should not un-contain b")
    assert(!matcher("ABC"), "ABC should not un-contain b since it's case insensitive")
    assert(matcher("acd"), "acd should un-contain b")
  }

}

@RunWith(classOf[JUnitRunner])
class MatcherRulesTest extends FunSuite {
  test("contain rule test") {

    val predicate = MatcherRules.apply("contain")
    assert(predicate("abc", "b"), "abc should contain b")

    assert(!predicate("abc", "d"), "abc should not contain d")

  }

  test("un-contain rule test") {

    val predicate = MatcherRules.apply("un-contain")
    assert(!predicate("abc", "b"), "abc should not un-contain b")

    assert(predicate("abc", "d"), "abc should un-contain d")

  }

  test("true rule test") {

    val predicate = MatcherRules.apply("true")
    assert(predicate("abc", "b"), "anything will be predicated to true")
    assert(predicate("abc", "d"), "anything will be predicated to true")
  }

  test("false rule test") {

    val predicate = MatcherRules.apply("false")
    assert(!predicate("abc", "b"), "anything will be predicated to false")
    assert(!predicate("abc", "d"), "anything will be predicated to false")
  }
}

