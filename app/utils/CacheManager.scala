package utils

import java.util.UUID
import play.cache.Cache
import java.util.concurrent.Callable

/**
 * Cache abstraction over Play Cache API, which provides support for multiple cache "buckets" using keys prefixing
 * @author Piotr Jędruszuk <pjedruszuk@semantive.com>

 * @param name logical key prefix
 * @tparam T cache data type
 */
class CacheManager[T](name: String) {

  val identifier : String = name + UUID.randomUUID().toString

  def apply(key: String) = get(key)

  def apply(key: String, function : String => T) = getOrElse(key, function)

  def get(key: String) = Cache.get(identifier + "_" + key).asInstanceOf[T]

  def getOrElse(key : String, function : String => T) = Cache.getOrElse(identifier + "_" + key, new Callable[T] {
    def call(): T = function(key)
  }, 432000)

  def ++(pair: (String, T)) = set(pair._1, pair._2)

  def ++(values : Map[String, T]) = setAll(values)

  def set(key: String, value: T) = Cache.set(identifier + "_" + key, value, 432000)

  def setAll(values : Map[String, T]) = for((key, value) <- values) set(key, value)
}
