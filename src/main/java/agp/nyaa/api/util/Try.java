package agp.nyaa.api.util;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Predicate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class Try<V> {

  public abstract V result();
  public abstract Exception exception();
  public abstract void throwException();
  public abstract Boolean isSuccess();
  public abstract Boolean isFailure();
  public abstract <U> Try<U> map(Function<? super V, ? extends U> f);
  public abstract <U> Try<U> flatMap(Function<? super V, Try<U>> f);
  public abstract <E extends RuntimeException> V orElseThrow(Function<Exception, E> f);


  public static <V> Try<V> failure(@NonNull final Exception t) {
    return new Failure<>(t);
  }

  public static <V> Try<V> success(@NonNull final V value) {
    return new Success<>(value);
  }

  public static <V> Try<V> call(@NonNull final Callable<V> f) {
    try {
      return Try.success(f.call());
    } catch (Exception t) {
      return Try.failure(t);
    }
  }

  @Getter
  @RequiredArgsConstructor
  @Accessors(fluent = true)
  private static final class Failure<V> extends Try<V> {

    @NonNull
    private final Exception exception;

    @Override
    public V result() {
      throwException();
      return null;
    }

    @Override
    public void throwException() {
      if (exception instanceof RuntimeException) {
        throw (RuntimeException) exception;
      } else {
        throw new RuntimeException(exception);
      }
    }

    @Override
    public Boolean isSuccess() {
      return false;
    }

    @Override
    public Boolean isFailure() {
      return true;
    }

    @Override
    public <U> Try<U> map(@NonNull final Function<? super V, ? extends U> f) {
      return Try.failure(exception);
    }

    @Override
    public <U> Try<U> flatMap(@NonNull final Function<? super V, Try<U>> f) {
      return Try.failure(exception);
    }

    @Override
    public <E extends RuntimeException> V orElseThrow(@NonNull final Function<Exception, E> f) {
      throw f.apply(exception);
    }
  }

  @Getter
  @RequiredArgsConstructor
  @Accessors(fluent = true)
  private static final class Success<V> extends Try<V> {

    @NonNull
    private final V result;

    @Override
    public Exception exception() {
      throwException();
      return null;
    }

    @Override
    public void throwException() {
      throw new IllegalStateException("Try was Success.");
    }

    @Override
    public Boolean isSuccess() {
      return true;
    }

    @Override
    public Boolean isFailure() {
      return false;
    }

    @Override
    public <U> Try<U> map(@NonNull final Function<? super V, ? extends U> f) {
      try {
        return Try.success(f.apply(result));
      } catch (Exception t) {
        return Try.failure(t);
      }
    }

    @Override
    public <U> Try<U> flatMap(@NonNull final Function<? super V, Try<U>> f) {
      try {
        return f.apply(result);
      } catch (Exception t) {
        return Try.failure(t);
      }
    }

    @Override
    public <E extends RuntimeException> V orElseThrow(@NonNull final Function<Exception, E> f) {
      return result();
    }
  }
}
