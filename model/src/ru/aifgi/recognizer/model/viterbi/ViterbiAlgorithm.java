package ru.aifgi.recognizer.model.viterbi;

/*
 * Copyright 2012 Alexey Ivanov
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

import ru.aifgi.recognizer.api.graph.Edge;
import ru.aifgi.recognizer.api.graph.Graph;
import ru.aifgi.recognizer.model.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aifgi
 */
public class ViterbiAlgorithm {
    public static int[] getLabels(final Graph graph) {
        final List<Pair<Edge, Integer>> path = findPath(graph);

        final int size = path.size();
        final int[] res = new int[size];
        for (int i = 0; i < size; ++i) {
            final Pair<Edge, Integer> pair = path.get(i);
            res[i] = pair.getFirst().getLabel(pair.getSecond());
        }
        return res;
    }

    private static List<Pair<Edge, Integer>> findPath(final Graph graph) {
        final int vertexNumber = graph.getVertexesNumber();
        final double[] vertexPenalties = new double[vertexNumber];
        final List<Pair<Edge, Integer>> path = new ArrayList<>();
        for (int i = 1; i < vertexNumber; ++i) {
            final Iterable<Edge> edgesToVertex = graph.getEdgesToVertex(i);
            double min = Double.MAX_VALUE;
            final Pair<Edge, Integer> minEdge = new Pair<>();
            for (final Edge edge : edgesToVertex) {
                final double previousPenalty = vertexPenalties[edge.getFromVertex()];
                final double[] weights = edge.getWeights();
                final int length = weights.length;
                for (int j = 0; j < length; ++j) {
                    final double weight = weights[j];
                    final double penalty = weight + previousPenalty;
                    if (penalty < min) {
                        min = penalty;
                        minEdge.set(edge, j);
                    }
                }
            }
            vertexPenalties[i] = min;
            path.add(minEdge);
        }
        return path;
    }

    private ViterbiAlgorithm() {
    }
}
