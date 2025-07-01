import { useState, useEffect } from 'react';
import { Droppable } from 'react-beautiful-dnd';

/**
 * Workaround component for React 18 StrictMode compatibility with react-beautiful-dnd
 * 
 * react-beautiful-dnd has issues with React 18's StrictMode because it uses
 * deprecated lifecycle methods. This component ensures the Droppable is only
 * rendered after the initial render, avoiding the StrictMode conflicts.
 */
const StrictModeDroppable = ({ children, ...props }: any) => {
  const [enabled, setEnabled] = useState(false);
  
  useEffect(() => {
    const animation = requestAnimationFrame(() => setEnabled(true));
    return () => {
      cancelAnimationFrame(animation);
      setEnabled(false);
    };
  }, []);
  
  if (!enabled) {
    return null;
  }
  
  return <Droppable {...props}>{children}</Droppable>;
};

export default StrictModeDroppable;
