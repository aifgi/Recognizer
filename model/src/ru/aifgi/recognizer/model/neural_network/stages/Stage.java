package ru.aifgi.recognizer.model.neural_network.stages;
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

import ru.aifgi.recognizer.model.neural_network.layers.StageOutput;

/**
 * @author aifgi
 */

public interface Stage {
    StageOutput forwardComputation(StageOutput input);

    StageOutput backwardComputation(final StageOutput stageOutput, final StageOutput errors);
}
