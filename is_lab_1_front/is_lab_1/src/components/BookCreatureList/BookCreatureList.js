import React, { useState, useEffect } from 'react';
import './BookCreatureList.css';
import CreatureListElement from '../CreatureListElement/CreatureListElement';
import FiltersBlock from '../FiltersBlock/FiltersBlock';
import Pagination from '../Pagination';
import Button from '../Button/Button';
import EditForm from '../EditForm/EditForm';

function BookCreatureList({ creatures = [], onUpdate, onDelete }) {
    const [selectedCreatures, setSelectedCreatures] = useState([]);
    const [nameFilter, setNameFilter] = useState('');
    const [ageFilter, setAgeFilter] = useState('');
    const [typeFilter, setTypeFilter] = useState('');
    const [locationFilter, setLocationFilter] = useState('');
    const [attackLevelFilter, setAttackLevelFilter] = useState('');
    const [defenseLevelFilter, setDefenseLevelFilter] = useState('');
    const [currentPage, setCurrentPage] = useState(1);
    const [editFormVisible, setEditFormVisible] = useState(false);
    const [selectedCreature, setSelectedCreature] = useState(null);
    const [showEditWarning, setShowEditWarning] = useState(false);
    const [sortField, setSortField] = useState(null);
    const [sortOrder, setSortOrder] = useState(null); // null - no sorting, 'asc' - ascending, 'desc' - descending
    const creaturesPerPage = 5;

    const handleSelectCreature = (id) => {
        const updatedSelection = selectedCreatures.includes(id)
            ? selectedCreatures.filter(creatureId => creatureId !== id) // Unselect if already selected
            : [...selectedCreatures, id]; // Select if not selected

        setSelectedCreatures(updatedSelection); // Update state with the new selection
        // Handle edit form visibility and selected creature
        if (updatedSelection.length === 0) {
            setEditFormVisible(false);
            setSelectedCreature(null);
            setShowEditWarning(false);
        } else if (updatedSelection.length === 1) {
            const creatureToEdit = creatures.find(creature => creature.id === updatedSelection[0]);
            setSelectedCreature(creatureToEdit);
            setEditFormVisible(true);
            setShowEditWarning(false);
        }
    };


    const handleMassDelete = () => {
        selectedCreatures.forEach(creatureId => onDelete(creatureId));
        setSelectedCreatures([]);
    };

    const handleEditSelected = () => {
        if (selectedCreatures.length === 1) {
            const creatureToEdit = creatures.find(creature => creature.id === selectedCreatures[0]);
            setSelectedCreature(creatureToEdit);
            setEditFormVisible(true);
            setShowEditWarning(false);
        } else {
            setShowEditWarning(true);
        }
    };

    const sortCreatures = (creatures) => {
        if (!sortField) return creatures; // No sorting applied

        return [...creatures].sort((a, b) => {
            const aValue = a[sortField];
            const bValue = b[sortField];

            if (aValue < bValue) return sortOrder === 'asc' ? -1 : 1;
            if (aValue > bValue) return sortOrder === 'asc' ? 1 : -1;
            return 0; // Values are equal
        });
    };


    const handleSort = (field) => {
        if (sortField === field) {
            // Toggle sort order
            setSortOrder((prevOrder) => (prevOrder === 'asc' ? 'desc' : 'asc'));
        } else {
            // Set new sort field and default to ascending
            setSortField(field);
            setSortOrder('asc');
        }
    };

    const filteredCreatures = creatures.filter(creature => {
        const matchesName = creature.name?.toLowerCase().includes(nameFilter.toLowerCase());
        const matchesAge = ageFilter ? creature.age === Number(ageFilter) : true;
        const matchesType = typeFilter ? creature.creatureType === typeFilter : true;
        const matchesLocation = creature.creatureLocation?.name?.toLowerCase().includes(locationFilter.toLowerCase());
        const matchesAttackLevel = attackLevelFilter ? creature.attackLevel >= Number(attackLevelFilter) : true;
        const matchesDefenseLevel = defenseLevelFilter ? creature.defenseLevel <= Number(defenseLevelFilter) : true;

        return (
            matchesName &&
            matchesAge &&
            matchesType &&
            matchesLocation &&
            matchesAttackLevel &&
            matchesDefenseLevel
        );
    });

    const sortedCreatures = sortCreatures(filteredCreatures);
    const totalCreatures = filteredCreatures.length;
    const totalPages = Math.ceil(totalCreatures / creaturesPerPage);
    const indexOfLastCreature = currentPage * creaturesPerPage;
    const indexOfFirstCreature = indexOfLastCreature - creaturesPerPage;
    const currentCreatures = filteredCreatures.slice(indexOfFirstCreature, indexOfLastCreature);

    return (
        <div className='book-creature-list'>
            <div className="filters-and-actions">
                <div className="filter-container">
                    <FiltersBlock filters={{
                        nameFilter, setNameFilter,
                        ageFilter, setAgeFilter,
                        typeFilter, setTypeFilter,
                        locationFilter, setLocationFilter,
                        attackLevelFilter, setAttackLevelFilter,
                        defenseLevelFilter, setDefenseLevelFilter
                    }} />
                </div>

                <div className="actions-block">
                    <Button
                        className="form__button button--rounded button--mini"
                        onClick={handleMassDelete}
                        label="Удалить выбранных"
                        disabled={selectedCreatures.length === 0}
                    />

                </div>
            </div>

            {showEditWarning && (
                <div className="no-selection-message">
                    Пожалуйста, выберите одно существо для редактирования.
                </div>
            )}

            <div className="table-container">
                <table className="creature-details-table">
                    <thead>
                    <tr>
                        <th>Выбрать</th>
                        <th>Имя</th>
                        <th>Координаты</th>
                        <th>Дата создания</th>
                        <th
                            onClick={() => handleSort('age')}
                            className={`sortable ${sortField === 'age' ? (sortOrder === 'asc' ? 'sort-asc' : 'sort-desc') : ''}`}>
                            Возраст
                        </th>
                        <th>Тип</th>
                        <th>Название города</th>
                        <th
                            onClick={() => handleSort('area')}
                            className={`sortable ${sortField === 'area' ? (sortOrder === 'asc' ? 'sort-asc' : 'sort-desc') : ''}`}>
                            Площадь
                        </th>
                        <th
                            onClick={() => handleSort('population')}
                            className={`sortable ${sortField === 'population' ? (sortOrder === 'asc' ? 'sort-asc' : 'sort-desc') : ''}`}>
                            Население
                        </th>
                        <th>Дата основания</th>
                        <th>Губернатор</th>
                        <th>Столица</th>
                        <th
                            onClick={() => handleSort('populationDensity')}
                            className={`sortable ${sortField === 'populationDensity' ? (sortOrder === 'asc' ? 'sort-asc' : 'sort-desc') : ''}`}>
                            Плотность населения
                        </th>
                        <th
                            onClick={() => handleSort('attackLevel')}
                            className={`sortable ${sortField === 'attackLevel' ? (sortOrder === 'asc' ? 'sort-asc' : 'sort-desc') : ''}`}>
                            Уровень атаки
                        </th>
                        <th
                            onClick={() => handleSort('defenseLevel')}
                            className={`sortable ${sortField === 'defenseLevel' ? (sortOrder === 'asc' ? 'sort-asc' : 'sort-desc') : ''}`}>
                            Уровень защиты
                        </th>
                        <th>Название кольца</th>
                        <th
                            onClick={() => handleSort('ringPower')}
                            className={`sortable ${sortField === 'ringPower' ? (sortOrder === 'asc' ? 'sort-asc' : 'sort-desc') : ''}`}>
                            Сила кольца
                        </th>
                        <th
                            onClick={() => handleSort('ringWeight')}
                            className={`sortable ${sortField === 'ringWeight' ? (sortOrder === 'asc' ? 'sort-asc' : 'sort-desc') : ''}`}>
                            Вес кольца
                        </th>
                        <th>Создатель</th>

                    </tr>
                    </thead>

                    <tbody>
                    {currentCreatures.map(creature => (
                        <CreatureListElement
                            key={creature.id}
                            creature={creature}
                            onUpdate={onUpdate}
                            onDelete={onDelete}
                            onSelect={handleSelectCreature}
                            isSelected={selectedCreatures.includes(creature.id)}
                            creatorName={creature.creatorName}
                        />
                    ))}
                    </tbody>

                </table>
            </div>

            <Pagination
                currentPage={currentPage}
                totalPages={totalPages}
                onPageChange={setCurrentPage}
            />

            {editFormVisible && selectedCreature && (
                <EditForm
                    visible={editFormVisible}
                    creature={selectedCreature}
                    onUpdate={(id, updatedCreature) => {
                        onUpdate(id, updatedCreature);
                        setEditFormVisible(false);
                    }}
                    func={setEditFormVisible}
                    className="edit-form"
                />
            )}
        </div>
    );
}

export default BookCreatureList;
