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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import ru.aifgi.recognizer.api.graph.Edge;
import ru.aifgi.recognizer.api.graph.Graph;

/**
 * @author aifgi
 */
public class GraphImpl implements Graph {
    private static class EdgeImpl implements Edge {
        final int from;
        final int to;
        final double[] weights;

        private EdgeImpl(final int from, final int to, final double[] weights) {
            this.from = from;
            this.to = to;
            this.weights = weights;
        }

        @Override
        public int getFromVertex() {
            return from;
        }

        @Override
        public int getToVertex() {
            return to;
        }

        @Override
        public double[] getWeights() {
            return weights;
        }

        @Override
        public int getLabel(final int index) {
            return index;
        }
    }

    private final int myVertexNumber;
    private final Multimap<Integer, Edge> myEdges = HashMultimap.create();

    public GraphImpl(final int vertexNumber) {
        myVertexNumber = vertexNumber;
    }

    @Override
    public void setWeights(final int from, final int to, final double[] weights) {
        myEdges.put(to, new EdgeImpl(from, to, weights));
    }

    @Override
    public int getVertexesNumber() {
        return myVertexNumber;
    }

    @Override
    public int getEdgesNumber() {
        return myEdges.size();
    }

    @Override
    public Iterable<Edge> getEdgesToVertex(final int vertex) {
        return myEdges.get(vertex);
    }
}
