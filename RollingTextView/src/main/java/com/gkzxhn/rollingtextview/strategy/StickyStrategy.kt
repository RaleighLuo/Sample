package com.gkzxhn.rollingtextview.strategy

import com.gkzxhn.rollingtextview.PreviousProgress


class StickyStrategy(val factor: Double) : NormalAnimationStrategy() {

    init {
        if (factor <= 0.0 && factor > 1.0) {
            throw IllegalStateException("factor must be in range (0,1] but now is $factor")
        }
    }

     override fun getFactor(previousProgress: PreviousProgress, index: Int, size: Int, charList: List<Char>): Double {
        return factor
    }
}