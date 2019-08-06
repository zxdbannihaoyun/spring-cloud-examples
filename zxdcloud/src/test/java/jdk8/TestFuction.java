package jdk8;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;


/**
 * Created by zxd on 2019/7/26.
 */
public class TestFuction {

    @Test
    public void test() {
        List<Double> list1 = Lists.newArrayList();
        list1.add(1.1d);
        list1.add(2.2d);
        list1.add(3.3d);

        List<Float> list2 = Lists.newArrayList();
        list2.add(1.4f);
        list2.add(2.1f);
        list2.add(3.4f);


        Lists.partition(list1,2);

        List<Double> list3 = Arrays.asList(1.0d, 2.1d, 3.3d);

        List<Boolean> result = listCombiner(list1, list2, (a, b) -> a > b);
//        assertThat(result).containsExactly(false, true, false);

        result.stream().forEach(System.out::println);
    }

    private <T, U, R> List<R> listCombiner(List<T> list1, List<U> list2, BiFunction<T, U, R> combiner) {
        List<R> result = Lists.newArrayList();
        for (int i = 0; i < list1.size(); i++) {
            result.add(combiner.apply(list1.get(i), list2.get(i)));
        }
        return result;
    }
}
