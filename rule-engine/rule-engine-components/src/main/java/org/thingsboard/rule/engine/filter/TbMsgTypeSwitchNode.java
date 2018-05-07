/**
 * Copyright © 2016-2018 The Thingsboard Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thingsboard.rule.engine.filter;

import lombok.extern.slf4j.Slf4j;
import org.thingsboard.rule.engine.TbNodeUtils;
import org.thingsboard.rule.engine.api.*;
import org.thingsboard.server.common.data.DataConstants;
import org.thingsboard.server.common.data.plugin.ComponentType;
import org.thingsboard.server.common.msg.TbMsg;
import org.thingsboard.server.common.msg.session.SessionMsgType;

@Slf4j
@RuleNode(
        type = ComponentType.FILTER,
        name = "message type switch",
        configClazz = EmptyNodeConfiguration.class,
        relationTypes = {"Post attributes", "Post telemetry", "RPC Request", "Activity Event", "Inactivity Event", "Connect Event", "Disconnect Event", "Other"},
        nodeDescription = "Route incoming messages by Message Type",
        nodeDetails = "Sends messages with message types <b>\"Post attributes\", \"Post telemetry\", \"RPC Request\"</b> via corresponding chain, otherwise <b>Other</b> chain is used.",
        uiResources = {"static/rulenode/rulenode-core-config.js"},
        configDirective = "tbNodeEmptyConfig")
public class TbMsgTypeSwitchNode implements TbNode {

    EmptyNodeConfiguration config;

    @Override
    public void init(TbContext ctx, TbNodeConfiguration configuration) throws TbNodeException {
        this.config = TbNodeUtils.convert(configuration, EmptyNodeConfiguration.class);
    }

    @Override
    public void onMsg(TbContext ctx, TbMsg msg) throws TbNodeException {
        String relationType;
        if (msg.getType().equals(SessionMsgType.POST_ATTRIBUTES_REQUEST.name())) {
            relationType = "Post attributes";
        } else if (msg.getType().equals(SessionMsgType.POST_TELEMETRY_REQUEST.name())) {
            relationType = "Post telemetry";
        } else if (msg.getType().equals(SessionMsgType.TO_SERVER_RPC_REQUEST.name())) {
            relationType = "RPC Request";
        } else if (msg.getType().equals(DataConstants.ACTIVITY_EVENT)) {
            relationType = "Activity Event";
        } else if (msg.getType().equals(DataConstants.INACTIVITY_EVENT)) {
            relationType = "Inactivity Event";
        } else if (msg.getType().equals(DataConstants.CONNECT_EVENT)) {
            relationType = "Connect Event";
        } else if (msg.getType().equals(DataConstants.DISCONNECT_EVENT)) {
            relationType = "Disconnect Event";
        } else {
            relationType = "Other";
        }
        ctx.tellNext(msg, relationType);
    }

    @Override
    public void destroy() {

    }
}
