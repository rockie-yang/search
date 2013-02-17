package search

import scala.Predef._

//trait StringMatcher {
//  def apply(origin: String): Boolean
//}

trait StringPredicate {
  def apply(content: String): Boolean
}

class StringMatcher(predicate: (String, String) => Boolean,
                    caseSensitive: Boolean, matchStrings: List[String]) extends StringPredicate {
  val matchStrs = if (caseSensitive) matchStrings else matchStrings.map(_.toLowerCase)

  def apply(content: String): Boolean = {

    val contentStr =
      if (caseSensitive) content
      else content.toLowerCase

    val result = matchStrs.forall(matchStr => predicate(contentStr, matchStr))

    //    println("match for " + content + " is " + result)

    result
  }
}

/**
 * It's a helper object to return predicate function
 * (String, String) => Boolean
 *
 * supported rules:
 * contain    : predicate to true, if the first string contains the second string
 * un_contain : predicate to true, if the first string does not contains the second string
 * true       : always predicate to true
 * false      : always predicate to false
 */
object MatcherRules {
  type Predicate = (String, String) => Boolean

//  def containRule(content: String, matchStr: String): Boolean =
//    content.contains(matchStr)
//  def unContainRule(content: String, matchStr: String): Boolean =
//    !content.contains(matchStr)

  def apply(rule: String): Predicate = rule match {
    case "contain" =>  // containRule
      (c: String, s: String) => c.contains(s)
    case "un-contain" => // unContainRule
      (c: String, s: String) => !c.contains(s)
    case "true" => (c: String, s: String) => true
    case "false" => (c: String, s: String) => false
    case _ => throw new Exception("The rule %s is not supported".format(rule))
  }
}

/**
 * It is a helper object to return a StringMatcher object based on the @rule,
 * whether caseSensitive,  and the list of string need be matched.
 *
 * for Rules refer to object $MatcherRules
 */
object StringMatcher {

  def apply(rule: String,
            caseSensitive: Boolean, matchStrings: List[String]): StringMatcher = {

    val predicate = MatcherRules(rule)

    new StringMatcher(predicate, caseSensitive, matchStrings)
  }
}

/**
 * It is a helper object to return a inverse StringMatcher object based on the @rule,
 * whether caseSensitive,  and the list of string need be matched.
 *
 * for Rules refer to object $MatcherRules
 */
object StringInverseMatcher {

  def apply(rule: String,
            ignoreCase: Boolean, matchStrings: List[String]) = {

    // if all matcher string is empty, then treat all match as true
    val deductedRule = if (matchStrings.forall(s => "" == s)) "true" else "un-" + rule

    val predicate = MatcherRules(deductedRule)

    new StringMatcher(predicate, ignoreCase, matchStrings)
  }
}

class StringMatchers(matchers: StringMatcher*) extends StringPredicate {
  def apply(content: String): Boolean = {
    matchers.forall(amatch => amatch(content))
  }
}


