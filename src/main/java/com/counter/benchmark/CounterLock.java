package com.counter.benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

import sun.misc.Lock;
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
@Fork(value = 3, warmups = 2)
@Warmup(iterations = 3)
@Measurement(iterations = 3)
public class CounterLock {
    private Lock lock = new Lock();
    private int count = 0;

    public static void main(String... args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(CounterLock.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    public int inc(){
        try {
            lock.lock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int newCount = ++count;
        lock.unlock();
        Blackhole.consumeCPU(newCount);
        return newCount;
    }
}
