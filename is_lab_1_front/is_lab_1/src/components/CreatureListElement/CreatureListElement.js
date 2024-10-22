import React, { useState } from 'react';
import './CreatureListElement.css';

const CreatureListElement = ({ creature, onUpdate, onDelete, onSelect, isSelected }) => {
    const [editMode, setEditMode] = useState(false);

    const date = new Date(creature.creationDate * 1000);
    const formattedDate = date.toLocaleString('ru-RU');

    return (
        <tr>
            <td>
                <input
                    type="checkbox"
                    checked={isSelected}
                    onChange={() => onSelect(creature.id)}
                />
            </td>
            <td>{creature.name}</td>
            <td>X: {creature.coordinates.x}, Y: {creature.coordinates.y}</td>
            <td>{formattedDate}</td>
            <td>{creature.age}</td>
            <td>{creature.creatureType}</td>
            <td>{creature.creatureLocation.name}</td>
            <td>{creature.creatureLocation.area}</td>
            <td>{creature.creatureLocation.population}</td>
            <td>{creature.creatureLocation.establishmentDate}</td>
            <td>{creature.creatureLocation.governor}</td>
            <td>{creature.creatureLocation.capital ? 'Да' : 'Нет'}</td>
            <td>{creature.creatureLocation.populationDensity}</td>
            <td>{creature.attackLevel}</td>
            <td>{creature.defenseLevel}</td>
            <td>{creature.ring.name}</td>
            <td>{creature.ring.power}</td>
            <td>{creature.ring.weight}</td>
            <td>{creature.creatorName}</td>
        </tr>
    );
};

export default CreatureListElement;
