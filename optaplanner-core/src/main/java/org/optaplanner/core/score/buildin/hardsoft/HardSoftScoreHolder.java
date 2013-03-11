/*
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.core.score.buildin.hardsoft;

import org.optaplanner.core.score.Score;
import org.optaplanner.core.score.holder.AbstractScoreHolder;
import org.kie.event.rule.ActivationUnMatchListener;
import org.kie.runtime.rule.Match;
import org.kie.runtime.rule.RuleContext;
import org.kie.runtime.rule.Session;

public class HardSoftScoreHolder extends AbstractScoreHolder {

    protected int hardScore;
    protected int softScore;

    public int getHardScore() {
        return hardScore;
    }

    public void setHardScore(int hardScore) {
        this.hardScore = hardScore;
    }

    public int getSoftScore() {
        return softScore;
    }

    public void setSoftScore(int softScore) {
        this.softScore = softScore;
    }

    // ************************************************************************
    // Worker methods
    // ************************************************************************

    public void addHardConstraintMatch(RuleContext kcontext, final int weight) {
        hardScore += weight;
        registerUndoListener(kcontext, new ActivationUnMatchListener() {
            public void unMatch(Session workingMemory, Match activation) {
                hardScore -= weight;
            }
        });
    }

    public void addSoftConstraintMatch(RuleContext kcontext, final int weight) {
        softScore += weight;
        registerUndoListener(kcontext, new ActivationUnMatchListener() {
            public void unMatch(Session workingMemory, Match activation) {
                softScore -= weight;
            }
        });
    }

    public Score extractScore() {
        return HardSoftScore.valueOf(hardScore, softScore);
    }

}