package com.hanframework.kit.boolfilter;

import com.google.common.hash.BloomFilter;

import com.google.common.hash.Funnel;

import java.nio.charset.Charset;

/**
 * @author liuxin
 * 2020-08-03 16:50
 */
public class BloomFilterTest {

    private static class User {

        private String name;

        private int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    public static void main(String[] args) {
        BloomFilter<User> bloomFilter = BloomFilter.create((Funnel<User>) (user, primitiveSink) -> primitiveSink.putString(user.getName(), Charset.defaultCharset())
                .putInt(user.getAge()), 10, 0.01);
        User xiaoming = new User("xiaoming", 1);
        bloomFilter.put(xiaoming);

        System.out.println(bloomFilter.mightContain(xiaoming));
        System.out.println(bloomFilter.mightContain(new User("xiaozhang", 2)));
    }
}
