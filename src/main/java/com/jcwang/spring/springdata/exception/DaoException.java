package com.jcwang.spring.springdata.exception;

import java.util.function.Supplier;

/**
 * 类描述
 *
 * @author jiancheng
 * @date 2020-6-30
 */
public class DaoException extends RuntimeException implements Supplier<DaoException> {


    public DaoException(String message) {
        super(message);
    }

    @Override
    public DaoException get() {
        return this;
    }
}
