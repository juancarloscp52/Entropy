/*
 * Copyright (c) 2021 juancarloscp52
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package me.juancarloscp52.entropy.client;

public class EntropyIntegrationsSettings {

    public int integrationType = 0;
    public String authToken = "";
    public String channel = "";
    public boolean sendChatMessages = true;
    public boolean showCurrentPercentage = true;

    public boolean showUpcomingEvents = true;

    public String discordToken = "";
    public long discordChannel = -1;
    public String youtubeClientId = "";
    public String youtubeSecret = "";
    public String youtubeAccessToken = "";
    public String youtubeRefreshToken = "";

}
