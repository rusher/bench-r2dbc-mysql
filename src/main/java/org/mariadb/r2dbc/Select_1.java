/*
 * Copyright 2020 MariaDB Ab.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mariadb.r2dbc;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;
import reactor.core.publisher.Flux;

import java.math.BigInteger;

public class Select_1 extends Common {

  @Benchmark
  public BigInteger testR2dbcMysql(MyState state, Blackhole blackhole) throws Throwable {
    int rnd = (int) (Math.random() * 1000);
    io.r2dbc.spi.Statement statement = state.r2dbcMysql.createStatement("select " + rnd);
    BigInteger val =
        Flux.from(statement.execute())
            .flatMap(it -> it.map((row, rowMetadata) -> row.get(0, BigInteger.class)))
            .blockLast();
    if (rnd != val.intValue())
      throw new IllegalStateException("ERROR rnd:" + rnd + " different to val:" + val);
    return val;
  }

}
