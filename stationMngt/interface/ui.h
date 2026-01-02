#ifndef UI_H
#define UI_H

#include "domain.h"

void ui_login(char *username, char *password);
void ui_menu_admin(void);
void ui_menu_operator(void);
void manager_get_sensor_data(void);
void manager_control_signal(void);
void manager_show_board(void);
void manager_manage_tracks(void);
void usac16_assign_track(void);
void usac16_set_free(void);
void usac16_set_inoperative(void);
void usac16_depart_train(void);

#endif
