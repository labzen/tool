@file:Suppress("unused")

package cn.labzen.tool.kotlin

import cn.labzen.tool.util.Strings
import cn.labzen.tool.util.Strings.at
import cn.labzen.tool.util.Strings.between
import cn.labzen.tool.util.Strings.blankToNull
import cn.labzen.tool.util.Strings.brief
import cn.labzen.tool.util.Strings.camelCase
import cn.labzen.tool.util.Strings.cut
import cn.labzen.tool.util.Strings.emptyToNull
import cn.labzen.tool.util.Strings.fill
import cn.labzen.tool.util.Strings.insert
import cn.labzen.tool.util.Strings.insureEndsWith
import cn.labzen.tool.util.Strings.insureStartsWith
import cn.labzen.tool.util.Strings.kebabCase
import cn.labzen.tool.util.Strings.repeatUntil
import cn.labzen.tool.util.Strings.simplify
import cn.labzen.tool.util.Strings.snakeCase
import cn.labzen.tool.util.Strings.studlyCase
import cn.labzen.tool.util.Strings.sub
import cn.labzen.tool.util.Strings.times
import cn.labzen.tool.util.Strings.wrap

/**
 * { IDIOMS } -- 如果字符串为empty，返回null
 *
 * @see Strings.emptyToNull
 */
fun String.toNullIfEmpty(): String? = emptyToNull(this)

/**
 * { IDIOMS } -- 如果字符串为blank，返回null
 *
 * @see Strings.blankToNull
 */
fun String.toNullIfBlank(): String? = blankToNull(this)

/**
 * { IDIOMS } -- 获取指定下标的单个字母字符串，index为负数时，从后向前找，index从0开始计数，0返回左侧第一个字符
 *
 * @see Strings.at
 */
fun String.at(index: Int): Char? = at(this, index)

/**
 * { IDIOMS } -- 截取从指定下标开始之前/之后的子字符串。index为截取的开始下标，为负数时，下标从源字符串最后向前确定。length决定子字符串的长度，当length为负数时，代表从后向前截取
 *
 * @see Strings.sub
 */
fun String.sub(start: Int, length: Int): String = sub(this, start, length)

/**
 * { IDIOMS } -- 格式化字符串，使用可变数量参数代替掉字符串中出现的{}
 *
 * @see Strings.format
 */
fun String.formatting(arguments: Iterable<Any?>) = Strings.format(this, arguments)

/**
 * { IDIOMS } -- 格式化字符串，使用可变数量参数代替掉字符串中出现的{}
 *
 * @see Strings.format
 */
fun String.formatting(vararg arguments: Any?) = Strings.format(this, arguments)

/**
 * { IDIOMS } -- 统计looking4子字符串在source字符串中出现的次数。从source中查找子字符串出现的开始位置，是在上一次找到的位置下标 + 子字符串的长度。也即是说查找"aaa"中"aa"出现的次数，这里是1次，而不是2次
 *
 * @see Strings.times
 */
fun String.times(looking4: String, ignoreCase: Boolean = false) = times(this, looking4, !ignoreCase)

/**
 * { IDIOMS } -- 获取指定的字符区间的内容
 *
 * @see Strings.between
 */
fun String.between(start: Char, end: Char) = between(this, start, end)

/**
 * { IDIOMS } -- 清除字符串中单词间多余的空格（单词间只保留一个空格），以及两侧的空格
 *
 * @see Strings.simplify
 */
fun String.simplify() = simplify(this)

/**
 * { IDIOMS } -- 转换成驼峰式，首字母大写
 *
 * @see Strings.studlyCase
 */
fun String.studlyCase() = studlyCase(this)

/**
 * { IDIOMS } -- 转换成驼峰式，首字母小写
 *
 * @see Strings.camelCase
 */
fun String.camelCase() = camelCase(this)

/**
 * { IDIOMS } -- 转换成Snake Case，以下划线间隔
 *
 * @see Strings.snakeCase
 */
fun String.snakeCase() = snakeCase(this)

/**
 * { IDIOMS } -- 转换成Kebab Case，以短横线间隔
 *
 * @see Strings.kebabCase
 */
fun String.kebabCase() = kebabCase(this)

/**
 * { IDIOMS } -- 重复给定的字符串直到字符串达到或超过给定长度
 *
 * @see Strings.repeatUntil
 */
fun String.repeatUntil(length: Int) = repeatUntil(this, length)

/**
 * { IDIOMS } -- 补充字符串达到给定长度， length如果为负数，将字符串填充至尾部。类似Guava的Strings.padStart()或Strings.padEnd()
 *
 * @see Strings.fill
 */
fun String.fill(filler: String, length: Int) = fill(this, filler, length)

/**
 * { IDIOMS } -- 插入子字符串到指定下标位置
 *
 * @see Strings.insert
 */
fun String.insert(index: Int, part: String) = insert(this, index, part)

/**
 * { IDIOMS } -- 确保字符串以给定的前缀开始
 *
 * @see Strings.insureStartsWith
 */
fun String.insureStartsWith(prefix: String, ignoreCase: Boolean = false) = insureStartsWith(this, prefix, !ignoreCase)

/**
 * { IDIOMS } -- 确保字符串以给定的后缀结束
 *
 * @see Strings.insureEndsWith
 */
fun String.insureEndsWith(suffix: String, ignoreCase: Boolean = false) = insureEndsWith(this, suffix, !ignoreCase)

/**
 * { IDIOMS } -- 获取字符串指定长度的缩略，尾部跟填充物
 *
 * @see Strings.brief
 */
fun String.brief(length: Int, filler: String) = brief(this, length, filler)

/**
 * { IDIOMS } -- 使用给定的前缀与后缀包裹指定的子字符串
 *
 * @see Strings.wrap
 */
fun String.wrap(looking4: String, prefix: String, suffix: String) = wrap(this, looking4, prefix, suffix)

/**
 * { IDIOMS } -- 按分隔符切分字符串为两部分
 *
 * @see Strings.cut
 */
fun String.cut(separator: String, ignoreCase: Boolean = false) = cut(this, separator, !ignoreCase)

/**
 * { IDIOMS } -- 判断与任意一个字符串是否相等
 */
fun String.equalsAny(vararg targets: String?, ignoreCase: Boolean = false) =
  targets.any { this.equals(it, ignoreCase) }
