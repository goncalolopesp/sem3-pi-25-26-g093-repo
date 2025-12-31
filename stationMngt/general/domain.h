#ifndef DOMAIN_H
#define DOMAIN_H

#include <time.h>

/* ---------- LIMITS ---------- */
#define MAX_USERS   10
#define MAX_TRACKS  10
#define MAX_LOGS    100

/* ---------- USERS ---------- */
typedef struct {
    char name[50];
        char username[20];
            char password[50];   // encrypted
                int cipher_key;
                } User;

                /* ---------- SENSORS ---------- */
                typedef struct {
                    int buffer_length;
                        int window_length;
                        } SensorConfig;

                        typedef struct {
                            float temperature;
                                float humidity;
                                    char temp_unit[10];
                                        char hum_unit[10];
                                            int valid;
                                            } SensorData;

                                            /* ---------- TRACKS ---------- */
                                            typedef enum {
                                                FREE,
                                                    BUSY,
                                                        INOPERATIVE
                                                        } TrackState;

                                                        typedef struct {
                                                            int id;
                                                                TrackState state;
                                                                    int train_id;
                                                                    } Track;

                                                                    /* ---------- TRAINS ---------- */
                                                                    typedef struct {
                                                                        int id;
                                                                        } Train;

                                                                        #endif
                                                                        
