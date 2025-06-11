/**
 * SnackbarService - Manages snackbar notifications and messages
 * Uses singleton pattern with subscription-based updates
 */

type SnackbarType = 'success' | 'error' | 'warning' | 'info';

type SnackbarState = {
  isVisible: boolean;
  message: string;
  type: SnackbarType;
};

type SnackbarUpdateCallback = (state: SnackbarState) => void;

class SnackbarService {
  private static instance: SnackbarService;
  private state: SnackbarState;
  private callbacks: Set<SnackbarUpdateCallback> = new Set();
  private autoCloseTimeout: number | null = null;

  private constructor() {
    this.state = {
      isVisible: false,
      message: '',
      type: 'info'
    };
  }

  public static getInstance(): SnackbarService {
    if (!SnackbarService.instance) {
      SnackbarService.instance = new SnackbarService();
    }
    return SnackbarService.instance;
  }

  public subscribe(callback: SnackbarUpdateCallback): () => void {
    this.callbacks.add(callback);
    // Return unsubscribe function
    return () => {
      this.callbacks.delete(callback);
    };
  }

  public getState(): SnackbarState {
    return { ...this.state };
  }

  private notifySubscribers(): void {
    this.callbacks.forEach(callback => callback(this.getState()));
  }

  private updateState(updates: Partial<SnackbarState>): void {
    this.state = { ...this.state, ...updates };
    this.notifySubscribers();
  }

  /**
   * Show a snackbar with the specified message and type
   * @param message - The message to display
   * @param type - The type of snackbar (success, error, warning, info)
   * @param autoClose - Whether to auto-close after 5 seconds (default: true)
   */
  public show(message: string, type: SnackbarType = 'info', autoClose: boolean = true): void {    // Clear any existing auto-close timeout
    if (this.autoCloseTimeout) {
      window.clearTimeout(this.autoCloseTimeout);
      this.autoCloseTimeout = null;
    }

    this.updateState({
      isVisible: true,
      message,
      type
    });

    // Auto-close after 5 seconds if enabled
    if (autoClose) {
      this.autoCloseTimeout = window.setTimeout(() => {
        this.close();
      }, 5000);
    }
  }

  /**
   * Show a success snackbar
   */
  public showSuccess(message: string, autoClose: boolean = true): void {
    this.show(message, 'success', autoClose);
  }

  /**
   * Show an error snackbar
   */
  public showError(message: string, autoClose: boolean = true): void {
    this.show(message, 'error', autoClose);
  }

  /**
   * Show a warning snackbar
   */
  public showWarning(message: string, autoClose: boolean = true): void {
    this.show(message, 'warning', autoClose);
  }

  /**
   * Show an info snackbar
   */
  public showInfo(message: string, autoClose: boolean = true): void {
    this.show(message, 'info', autoClose);
  }

  /**
   * Close the current snackbar
   */
  public close(): void {    // Clear auto-close timeout if it exists
    if (this.autoCloseTimeout) {
      window.clearTimeout(this.autoCloseTimeout);
      this.autoCloseTimeout = null;
    }

    this.updateState({ isVisible: false });
  }

  /**
   * Check if snackbar is currently visible
   */
  public isVisible(): boolean {
    return this.state.isVisible;
  }

  /**
   * Get current message
   */
  public getCurrentMessage(): string {
    return this.state.message;
  }

  /**
   * Get current type
   */
  public getCurrentType(): SnackbarType {
    return this.state.type;
  }

  /**
   * Clear the snackbar state completely
   */
  public clear(): void {
    if (this.autoCloseTimeout) {
      clearTimeout(this.autoCloseTimeout);
      this.autoCloseTimeout = null;
    }

    this.state = {
      isVisible: false,
      message: '',
      type: 'info'
    };
    this.notifySubscribers();
  }
}

export const snackbarService = SnackbarService.getInstance();
