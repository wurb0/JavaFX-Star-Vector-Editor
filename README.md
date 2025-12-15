JavaFX Star Vector Editor

An interactive vector graphics editor built in JavaFX, demonstrating advanced GUI interaction techniques, a full MVC architecture, and editor-style features such as grouping, handles, and undo/redo.

This project focuses on direct manipulation, transform-based rendering, and robust interaction state management, similar to simplified vector design tools.

Overview

The editor allows users to create, select, manipulate, group, and transform four-pointed star shapes using mouse and keyboard interactions.
All interactions are implemented using patterns and techniques taught in modern GUI and interaction design, emphasizing clean separation of concerns and extensibility.

The system is implemented using:

Model–View–Controller (MVC)

An explicit Interaction Model

Immediate-mode rendering

The Command pattern for undo/redo

Core Features
Star Creation

Stars are created by clicking and dragging on the canvas

Creation only occurs when dragging down and right (invalid drags are ignored)

A preview star is shown during creation

Selection

Click to select a star or group

Ctrl + Click enables multi-selection

Right-click clears the current selection

Selected items are visually highlighted

Direct Manipulation

Move: drag selected items

Resize: drag the resize handle (bottom-right)

Rotate: drag the rotation handle above the object

All transformations are applied using affine transforms, not recomputed geometry

Grouping and Hierarchy

Multiple selected items can be grouped

Groups can contain stars or other groups (hierarchical grouping)

Groups can be manipulated as a single unit

Ungrouping restores original relative positions and rotations

Undo / Redo

Fully implemented using the Command pattern

Supported commands include:

Create

Delete

Move

Resize

Rotate

Group

Ungroup

Undo (Z) and Redo (R) correctly restore previous states

Architecture
MVC Structure

Model (StarModel)

Stores all drawable objects

Manages grouping and hierarchy

Notifies views via publish–subscribe

View (StarView)

Immediate-mode rendering using GraphicsContext

Uses an offscreen bitmap for hit-testing

Draws selection outlines, handles, and previews

Controller (StarController)

Interprets all mouse and keyboard input

Implements a state-machine-based interaction controller

Interaction Model (InteractionModel)

Stores transient interaction state (mode, selection, drag deltas)

Maintains undo/redo stacks

Decouples interaction logic from rendering and data storage

Interaction Techniques Demonstrated

Offscreen color-based picking

Handle-based resizing and rotation

Multi-select with modifier keys

Group-level transformations

Immediate visual feedback during interaction

Robust undo/redo for complex operations

Controls Summary
Action	Input
Create star	Click + drag
Select	Click
Multi-select	Ctrl + Click
Move	Drag selected item
Resize	Drag resize handle
Rotate	Drag rotation handle
Group	G
Ungroup	U
Delete	Delete / Backspace
Undo	Z
Redo	R
Clear selection	Right-click
Requirements & Dependencies

Java 17+

JavaFX (no external libraries required)

The project runs by opening the project and pressing Run.
