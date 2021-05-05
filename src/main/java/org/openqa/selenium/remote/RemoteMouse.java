// Licensed to the Software Freedom Conservancy (SFC) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The SFC licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.openqa.selenium.remote;

import com.google.common.collect.ImmutableMap;
import com.salesforce.drillbit.client.EventDispatcher;

import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Mouse;

import java.util.HashMap;
import java.util.Map;

/**
 * Executes wire commands for mouse interaction.
 */
public class RemoteMouse implements Mouse {
	protected final ExecuteMethod executor;

	private EventDispatcher eventDispatcher;

	public RemoteMouse(ExecuteMethod executor) {
		this.executor = executor;
		this.eventDispatcher = EventDispatcher.getInstance();
	}

	protected Map<String, Object> paramsFromCoordinates(Coordinates where) {
		Map<String, Object> params = new HashMap<>();

		if (where != null) {
			String id = (String) where.getAuxiliary();
			params.put("element", id);
		}

		return params;
	}

	protected void moveIfNeeded(Coordinates where) {
		if (where != null) {
			innerMouseMove(where);
		}
	}

	public void click(Coordinates where) {
		moveIfNeeded(where);
	
		eventDispatcher.beforeClickByMouse(where);
		executor.execute(DriverCommand.CLICK, ImmutableMap.of("button", 0));
		eventDispatcher.afterClickByMouse(where);
	}

	public void contextClick(Coordinates where) {
		moveIfNeeded(where);

		eventDispatcher.beforeContextClick(where);
		executor.execute(DriverCommand.CLICK, ImmutableMap.of("button", 2));
		eventDispatcher.afterContextClick(where);
	}

	public void doubleClick(Coordinates where) {
		moveIfNeeded(where);

		eventDispatcher.beforeDoubleClick(where);
		executor.execute(DriverCommand.DOUBLE_CLICK, ImmutableMap.of());
		eventDispatcher.afterDoubleClick(where);
	}

	public void mouseDown(Coordinates where) {
		moveIfNeeded(where);

		eventDispatcher.beforeMouseDown(where);
		executor.execute(DriverCommand.MOUSE_DOWN, ImmutableMap.of());
		eventDispatcher.afterMouseDown(where);
	}

	public void mouseUp(Coordinates where) {
		moveIfNeeded(where);

		eventDispatcher.beforeMouseUp(where);
		executor.execute(DriverCommand.MOUSE_UP, ImmutableMap.of());
		eventDispatcher.afterMouseUp(where);
	}

	public void mouseMove(Coordinates where) {
		eventDispatcher.beforeMouseMove(where);
		innerMouseMove(where);
		eventDispatcher.afterMouseMove(where);
	}

	public void mouseMove(Coordinates where, long xOffset, long yOffset) {
		Map<String, Object> moveParams = paramsFromCoordinates(where);
		moveParams.put("xoffset", xOffset);
		moveParams.put("yoffset", yOffset);

		eventDispatcher.beforeMouseMove(where, xOffset, yOffset);
		executor.execute(DriverCommand.MOVE_TO, moveParams);
		eventDispatcher.afterMouseMove(where, xOffset, yOffset);
	}

	private void innerMouseMove(Coordinates where) {
		Map<String, Object> moveParams = paramsFromCoordinates(where);

		executor.execute(DriverCommand.MOVE_TO, moveParams);
	}
}
