    .globl update_track
        .text

        # int update_track(Track *t, int new_state, int new_train_id)
        # a0 = t (Track*)
        # a1 = new_state
        # a2 = new_train_id
        # return a0 = 1 success, 0 fail

        update_track:
            beqz a0, fail          # if t == NULL -> fail

                sw a1, 4(a0)           # t->state = new_state
                    sw a2, 8(a0)           # t->train_id = new_train_id

                        li a0, 1
                            ret

                            fail:
                                li a0, 0
                                    ret
                                    
