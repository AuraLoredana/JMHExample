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
import java.util.concurrent.locks.ReentrantLock;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 3, warmups = 2)
@Warmup(iterations = 3)
@Measurement(iterations = 3)
public class CounterReentrantLock {
    ReentrantLock lock = new ReentrantLock();
    int count = 0;

    public static void main(String... args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(CounterReentrantLock.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    public int getValue() {
        lock.lock();
        try {
            Blackhole.consumeCPU(count);
            return count;
        } finally {
            lock.unlock();
        }
    }

    @Benchmark
    public void increment() {
        lock.lock();
        try {
            Blackhole.consumeCPU(count++);
        } finally {
            lock.unlock();
        }
    }
}
